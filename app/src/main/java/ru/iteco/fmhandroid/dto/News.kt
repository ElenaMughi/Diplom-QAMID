package ru.iteco.fmhandroid.dto

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import ru.iteco.fmhandroid.entity.NewsCategoryEntity

@kotlinx.parcelize.Parcelize
data class News(
    val id: Int? = null,
    val newsCategoryId: Int,
    val title: String = "",
    val description: String = "",
    val creatorId: Int = 1,
    val creatorName: String,
    val createDate: Long,
    val publishDate: Long,
    val publishEnabled: Boolean = false,
    val isOpen: Boolean = false
) : Parcelable {
    @kotlinx.parcelize.Parcelize
    data class Category(
        val id: Int,
        val name: String,
        val deleted: Boolean
    ) : Parcelable {

        enum class Type {
            Advertisement, Salary, Union, Birthday, Holiday, Massage, Gratitude, Help, Unknown
        }
    }
}

@kotlinx.parcelize.Parcelize
data class NewsWithCategory(
    @Embedded
    val newsItem: News,
    @Relation(
        entity = NewsCategoryEntity::class,
        parentColumn = "newsCategoryId",
        entityColumn = "id"
    )
    val category: News.Category
) : Parcelable

@kotlinx.parcelize.Parcelize
data class NewsFilterArgs(
    val category: String? = null,
    val dates: List<Long>? = null,
    val status: Boolean? = null
) : Parcelable
