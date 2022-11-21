package ru.iteco.fmhandroid.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.iteco.fmhandroid.dto.Claim
import ru.iteco.fmhandroid.dto.FullClaim
import ru.iteco.fmhandroid.entity.ClaimEntity

@Dao
interface ClaimDao {
    @Transaction
    @Query("SELECT * FROM ClaimEntity")
    suspend fun getAllClaims(): List<FullClaim>

    @Transaction
    @Query(
        """
       SELECT * FROM ClaimEntity
       WHERE (status IN (:listStatuses))
       ORDER BY planExecuteDate ASC, createDate DESC
    """
    )
    fun getClaimsByStatus(
        listStatuses: List<Claim.Status>
    ): Flow<List<FullClaim>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClaim(claim: ClaimEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClaim(claims: List<ClaimEntity>)

    @Transaction
    @Query("SELECT * FROM ClaimEntity WHERE id = :id")
    fun getClaimById(id: Int): Flow<FullClaim>

    @Query("DELETE FROM ClaimEntity WHERE id IN (:idList)")
    suspend fun removeClaimsItemsByIdList(idList: List<Int?>)
}

class ClaimClaimStatusConverter {

    @TypeConverter
    fun toClaimStatus(status: String): Claim.Status = Claim.Status.valueOf(status)

    @TypeConverter
    fun fromClaimStatus(status: Claim.Status) = status.name
}
