package ru.iteco.fmhandroid.exceptions

import android.database.SQLException
import java.io.IOException
import java.net.ConnectException

sealed class AppException(var code: String) : RuntimeException() {
    companion object {
        fun from(e: Throwable): AppException = when (e) {
            is AppException -> e
            is IllegalArgumentException -> AuthorizationException
            is SQLException -> DbException
            is IOException -> ServerException
            is ConnectException -> LostConnectException
            else -> UnknownException
        }
    }
}

class ApiException(val statusCode: Int, code: String) : AppException(code)
object AuthorizationException : AppException("authorization_failed")
object ServerException : AppException("error_server")
object DbException : AppException("error_db")
object LostConnectException : AppException("error_connect")
object UnknownException : AppException("error_unknown")
