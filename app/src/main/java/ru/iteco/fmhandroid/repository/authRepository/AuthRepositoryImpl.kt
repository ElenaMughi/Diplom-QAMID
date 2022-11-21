package ru.iteco.fmhandroid.repository.authRepository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.iteco.fmhandroid.api.AuthApi
import ru.iteco.fmhandroid.api.RefreshTokensApi
import ru.iteco.fmhandroid.auth.AppAuth
import ru.iteco.fmhandroid.dto.AuthState
import ru.iteco.fmhandroid.dto.JwtResponse
import ru.iteco.fmhandroid.dto.LoginData
import ru.iteco.fmhandroid.dto.RefreshRequest
import ru.iteco.fmhandroid.exceptions.ApiException
import ru.iteco.fmhandroid.exceptions.AuthorizationException
import ru.iteco.fmhandroid.exceptions.UnknownException
import ru.iteco.fmhandroid.utils.Utils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val refreshTokensApi: RefreshTokensApi,
    private val appAuth: AppAuth
) : AuthRepository {

    override suspend fun login(login: String, password: String) =
        Utils.makeRequest(
            request = { authApi.getTokens(LoginData(login = login, password = password)) },
            onSuccess = { body -> appAuth.authState = body },
            onFailure = {
                // Было бы здорово вынести этот код в отдельную функцию.
                val gson = Gson()
                val type = object : TypeToken<JwtResponse>() {}.type
                val errorResponse: JwtResponse? = gson.fromJson(it.errorBody()?.charStream(), type)
                if (errorResponse?.message.equals("ERR_INVALID_LOGIN")) {
                    throw AuthorizationException
                } else {
                    throw UnknownException
                }
            }
        )

    override suspend fun updateTokens(refreshToken: String): AuthState? =
        Utils.makeRequest(
            request = {
                refreshTokensApi.refreshTokens(
                    refreshToken,
                    RefreshRequest(refreshToken)
                )
            },
            onSuccess = { body ->
                appAuth.authState = body
                body
            },
            onFailure = {
                if (it.code() == 401) {
                    appAuth.authState = null
                    null
                } else {
                    throw ApiException(it.code(), it.message())
                }
            }
        )
}
