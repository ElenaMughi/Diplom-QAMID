package ru.iteco.fmhandroid.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.iteco.fmhandroid.dto.AuthState
import ru.iteco.fmhandroid.dto.LoginData

interface AuthApi {
    @POST("authentication/login")
    suspend fun getTokens(
        @Body loginData: LoginData
    ): Response<AuthState>
}
