package ru.iteco.fmhandroid.ui.viewdata

import ru.iteco.fmhandroid.dto.News

/**
 * Модель представления новости в списке
 */
data class NewsViewData(
    val id: Int,
    val category: News.Category,
    val title: String,
    val description: String,
    val creatorId: Int,
    val creatorName: String,
    val createDate: Long,
    val publishDate: Long,
    val publishEnabled: Boolean,
    val isOpen: Boolean
)
