package ru.iteco.fmhandroid.repository.authRepository

import ru.iteco.fmhandroid.dto.AuthState

interface AuthRepository {
    suspend fun login(login: String, password: String)

    /**
     * Обновляет токены при помощи [refreshToken]
     *
     * @return обновленные токены.
     * Если [refreshToken] истек возвращает `null`.
     */
    suspend fun updateTokens(refreshToken: String): AuthState?
}
