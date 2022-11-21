package ru.iteco.fmhandroid.repository.userRepository

import ru.iteco.fmhandroid.dto.User

interface UserRepository {
    val currentUser: User
    val userList: List<User>
    suspend fun getAllUsers(): List<User>
    suspend fun getUserInfo()
    fun userLogOut()
}