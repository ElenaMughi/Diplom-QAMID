package ru.iteco.fmhandroid.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClaimComment(
    val id: Int? = null,
    val claimId: Int,
    val description: String,
    val creatorId: Int,
    val creatorName: String,
    val createDate: Long,
) : Parcelable
