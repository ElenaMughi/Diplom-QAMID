package ru.iteco.fmhandroid.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.EditText
import retrofit2.Response
import ru.iteco.fmhandroid.dto.ClaimComment
import ru.iteco.fmhandroid.dto.User
import ru.iteco.fmhandroid.exceptions.*
import java.io.IOException
import java.net.ConnectException
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

object Utils {
    object Empty {
        val emptyClaimComment = ClaimComment(
            id = 0,
            claimId = 0,
            description = "",
            creatorId = 0,
            creatorName = "",
            createDate = 0,
        )
        val emptyUser = User(
            id = 0,
            admin = false,
            firstName = "",
            lastName = "",
            middleName = "",
        )
    }

    fun convertNewsCategory(category: String): Int {
        return when (category) {
            "Объявление" -> 1
            "День рождения" -> 2
            "Зарплата" -> 3
            "Профсоюз" -> 4
            "Праздник" -> 5
            "Массаж" -> 6
            "Благодарность" -> 7
            "Нужна помощь" -> 8
            else -> 0
        }
    }

    fun updateDateLabel(calendar: Calendar, editText: EditText) {
        val format = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        editText.setText(simpleDateFormat.format(calendar.time))
    }

    fun updateTimeLabel(calendar: Calendar, editText: EditText) {
        val format = "HH:mm"
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        editText.setText(simpleDateFormat.format(calendar.time))
    }

    //Save date and time from pickers, and convert it to String in fragment
    fun saveDateTime(date: String, time: String): Long {
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val localDate = LocalDate.parse(date, dateFormatter)
        val localTime = LocalTime.parse(time, timeFormatter)
        val localDateTime = LocalDateTime.of(localDate, localTime)
        return fromLocalDateTimeToTimeStamp(localDateTime)
    }

    fun fromLocalDateTimeToTimeStamp(date: LocalDateTime): Long {
        return date.toEpochSecond(
            ZoneId.systemDefault()
                .rules.getOffset(Instant.now())
        )
    }

    fun formatDate(date: Long): String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(date),
            ZoneId.systemDefault()
        )
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
            "dd.MM.yyy",
            Locale.getDefault()
        )
        return formatter.format(localDateTime)
    }

    fun formatTime(date: Long): String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(date),
            ZoneId.systemDefault()
        )
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
            "HH:mm",
            Locale.getDefault()
        )
        return formatter.format(localDateTime)
    }

    suspend fun <T, R> makeRequest(
        request: suspend () -> Response<T>,
        onSuccess: suspend (body: T) -> R,
        onFailure: (response: Response<T>) -> R = { throw ApiException(it.code(), it.message()) }
    ): R {
        try {
            val response = request()
            if (!response.isSuccessful) return onFailure(response)
            val body =
                response.body() ?: throw ApiException(response.code(), response.message())
            return onSuccess(body)
        } catch (e: ConnectException) {
            throw LostConnectException
        } catch (e: IOException) {
            throw ServerException
        } catch (e: AuthorizationException) {
            throw AuthorizationException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    fun fullUserNameGenerator(lastName: String, firstName: String, middleName: String): String {
        return "$lastName $firstName $middleName"
    }

    fun generateShortUserName(fullName: String): String {
        val parsedName = fullName.trim().split("\\s".toRegex())
        return "${parsedName[0]} ${parsedName[1].first().uppercase()}.${
            parsedName[2].first().uppercase()
        }."
    }

    fun isOnline(context: Context): Boolean {
        var result = false // Returns connection. false: none; true: mobile data or wifi
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            result = true
                        }
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            result = true
                        }
                        hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                            result = true
                        }
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    when (type) {
                        ConnectivityManager.TYPE_WIFI -> {
                            result = true
                        }
                        ConnectivityManager.TYPE_MOBILE -> {
                            result = true
                        }
                        ConnectivityManager.TYPE_VPN -> {
                            result = true
                        }
                    }
                }
            }
        }
        return result
    }
}
