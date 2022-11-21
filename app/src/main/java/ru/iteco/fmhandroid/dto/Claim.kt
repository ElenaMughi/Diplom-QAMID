package ru.iteco.fmhandroid.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Claim(
    val id: Int? = null,
    val title: String,
    val description: String,
    val creatorId: Int,
    val creatorName: String,
    var executorId: Int? = null,
    val executorName: String? = null,
    val createDate: Long,
    val planExecuteDate: Long,
    val factExecuteDate: Long? = null,
    var status: Status,
) : Parcelable {

    enum class Status {
        CANCELLED,
        EXECUTED,
        IN_PROGRESS,
        OPEN
    }
}
