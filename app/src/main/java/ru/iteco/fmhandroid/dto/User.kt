package ru.iteco.fmhandroid.dto

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class User(
    var id: Int,
    val admin: Boolean,
    val firstName: String,
    val lastName: String,
    val middleName: String,
) : Parcelable
