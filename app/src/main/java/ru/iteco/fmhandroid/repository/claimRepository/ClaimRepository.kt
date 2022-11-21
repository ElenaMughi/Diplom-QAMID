package ru.iteco.fmhandroid.repository.claimRepository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import ru.iteco.fmhandroid.dto.Claim
import ru.iteco.fmhandroid.dto.ClaimComment
import ru.iteco.fmhandroid.dto.FullClaim

interface ClaimRepository {
    fun getClaimsByStatus(
        coroutineScope: CoroutineScope,
        listStatuses: List<Claim.Status>
    ): Flow<List<FullClaim>>

    suspend fun refreshClaims()
    suspend fun modificationOfExistingClaim(editedClaim: Claim): Claim
    suspend fun createNewClaim(claim: Claim): Claim
    fun getClaimById(id: Int): Flow<FullClaim>
    suspend fun getAllCommentsForClaim(id: Int): List<ClaimComment>
    suspend fun saveClaimComment(claimId: Int, comment: ClaimComment): ClaimComment

    /**
     * При переводе заявки из статуса OPEN -> IN PROGRESS заявке назначается исполнитель
     * При переводе заявки из статуса IN PROGRESS -> OPEN с заявки снимается исполнитель
     * При действиях "Сбросить" и "Исполнить" исполнитель обязан оставить объясняющий комментарий
     * **/
    suspend fun changeClaimStatus(
        claimId: Int,
        newStatus: Claim.Status,
        executorId: Int?,
        claimComment: ClaimComment
    ): Claim

    suspend fun changeClaimComment(comment: ClaimComment): ClaimComment
    suspend fun getAllClaimsWithOpenAndInProgressStatus(): List<Claim>
}
