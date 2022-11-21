package ru.iteco.fmhandroid.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.iteco.fmhandroid.dto.Claim

@Entity(tableName = "ClaimEntity")
data class ClaimEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "creatorId")
    val creatorId: Int,
    @ColumnInfo(name = "creatorName")
    val creatorName: String,
    @ColumnInfo(name = "executorId")
    val executorId: Int?,
    @ColumnInfo(name = "executorName")
    val executorName: String?,
    @ColumnInfo(name = "createDate")
    val createDate: Long,
    @ColumnInfo(name = "planExecuteDate")
    val planExecuteDate: Long,
    @ColumnInfo(name = "factExecuteDate")
    val factExecuteDate: Long?,
    @ColumnInfo(name = "status")
    val status: Claim.Status,
)

fun List<Claim>.toEntity(): List<ClaimEntity> = map(Claim::toEntity)
fun Claim.toEntity() = ClaimEntity(
    id = id,
    title = title,
    description = description,
    creatorId = creatorId,
    creatorName = creatorName,
    executorId = executorId,
    executorName = executorName,
    createDate = createDate,
    planExecuteDate = planExecuteDate,
    factExecuteDate = factExecuteDate,
    status = status
)
