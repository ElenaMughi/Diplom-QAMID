package ru.iteco.fmhandroid.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.iteco.fmhandroid.dto.AuthState
import ru.iteco.fmhandroid.dto.RefreshRequest

interface RefreshTokensApi {
    @POST("authentication/refresh")
    suspend fun refreshTokens(
        @Header("Authorization") refreshToken: String,
        @Body refreshRequest: RefreshRequest
    ): Response<AuthState>
}
