package ru.iteco.fmhandroid.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.iteco.fmhandroid.dao.*
import ru.iteco.fmhandroid.entity.ClaimCommentEntity
import ru.iteco.fmhandroid.entity.ClaimEntity
import ru.iteco.fmhandroid.entity.NewsCategoryEntity
import ru.iteco.fmhandroid.entity.NewsEntity

@Database(
    entities = [
        ClaimEntity::class,
        ClaimCommentEntity::class,
        NewsEntity::class,
        NewsCategoryEntity::class,
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(
    ClaimClaimStatusConverter::class
)
abstract class AppDb : RoomDatabase() {
    abstract fun getClaimDao(): ClaimDao
    abstract fun getClaimCommentDao(): ClaimCommentDao
    abstract fun getNewsDao(): NewsDao
    abstract fun getNewsCategoryDao(): NewsCategoryDao
}
