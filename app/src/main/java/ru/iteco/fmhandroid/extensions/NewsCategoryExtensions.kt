package ru.iteco.fmhandroid.extensions

import ru.iteco.fmhandroid.dto.News

fun News.Category.getType(): News.Category.Type =
    when (id) {
        1 -> News.Category.Type.Advertisement
        2 -> News.Category.Type.Birthday
        3 -> News.Category.Type.Salary
        4 -> News.Category.Type.Union
        5 -> News.Category.Type.Holiday
        6 -> News.Category.Type.Massage
        7 -> News.Category.Type.Gratitude
        8 -> News.Category.Type.Help
        else -> News.Category.Type.Unknown
    }
