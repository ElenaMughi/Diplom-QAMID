package ru.iteco.fmhandroid.api

import okhttp3.Interceptor
import okhttp3.Response
import ru.iteco.fmhandroid.auth.AppAuth

class RefreshInterceptor(private val auth: AppAuth) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = checkNotNull(auth.authState) {
            "User must be authorized"
        }.refreshToken

        val newRequest = chain.request().newBuilder()
            .header("Authorization", token)
            .build()
        return chain.proceed(newRequest)
    }
}
