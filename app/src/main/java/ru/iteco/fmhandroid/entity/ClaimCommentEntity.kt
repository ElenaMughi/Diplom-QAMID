package ru.iteco.fmhandroid.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.iteco.fmhandroid.dto.ClaimComment

@Entity(tableName = "ClaimCommentEntity")
data class ClaimCommentEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "claimId")
    val claimId: Int,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "creatorId")
    val creatorId: Int,
    @ColumnInfo(name = "creatorName")
    val creatorName: String,
    @ColumnInfo(name = "createDate")
    val createDate: Long
)

fun List<ClaimComment>.toEntity(): List<ClaimCommentEntity> = map(ClaimComment::toEntity)
fun ClaimComment.toEntity() = ClaimCommentEntity(
    id = id,
    claimId = claimId,
    description = description,
    creatorId = creatorId,
    creatorName = creatorName,
    createDate = createDate
)
