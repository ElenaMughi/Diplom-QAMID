package ru.iteco.fmhandroid.dto

import android.annotation.SuppressLint

@SuppressLint("CustomSplashScreen")
data class SplashScreenData(
    val image: Int,
    val title: String,
    val titleBackground: Int,
    val titleColor: Int,
)
