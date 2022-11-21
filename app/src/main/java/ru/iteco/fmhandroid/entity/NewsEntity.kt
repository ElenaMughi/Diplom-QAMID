package ru.iteco.fmhandroid.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.iteco.fmhandroid.dto.News

@Entity(tableName = "NewsEntity")
data class NewsEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "newsCategoryId")
    val newsCategoryId: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "creatorId")
    val creatorId: Int,
    @ColumnInfo(name = "creatorName")
    val creatorName: String,
    @ColumnInfo(name = "createDate")
    val createDate: Long,
    @ColumnInfo(name = "publishDate")
    val publishDate: Long,
    @ColumnInfo(name = "publishEnabled")
    val publishEnabled: Boolean = false,
    @ColumnInfo(name = "isOpen")
    val isOpen: Boolean = false,
)

fun List<News>.toEntity(): List<NewsEntity> = map(News::toEntity)
fun News.toEntity() = NewsEntity(
    id = id,
    newsCategoryId = newsCategoryId,
    title = title,
    description = description,
    creatorId = creatorId,
    creatorName = creatorName,
    createDate = createDate,
    publishDate = publishDate,
    publishEnabled = publishEnabled,
    isOpen = isOpen,
)

@Entity(tableName = "NewsCategoryEntity")
data class NewsCategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "deleted")
    val deleted: Boolean
) {
    fun toDto() = News.Category(
        id = id,
        name = name,
        deleted = deleted,
    )
}

fun List<NewsCategoryEntity>.toNewsCategoryDto(): List<News.Category> =
    map(NewsCategoryEntity::toDto)

fun List<News.Category>.toNewsCategoryEntity(): List<NewsCategoryEntity> =
    map(News.Category::toNewsCategoryEntity)

fun News.Category.toNewsCategoryEntity() = NewsCategoryEntity(
    id = id,
    name = name,
    deleted = deleted,
)
