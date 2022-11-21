package ru.iteco.fmhandroid.repository.claimRepository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import ru.iteco.fmhandroid.api.ClaimApi
import ru.iteco.fmhandroid.dao.ClaimCommentDao
import ru.iteco.fmhandroid.dao.ClaimDao
import ru.iteco.fmhandroid.dto.Claim
import ru.iteco.fmhandroid.dto.ClaimComment
import ru.iteco.fmhandroid.entity.toEntity
import ru.iteco.fmhandroid.utils.Utils.makeRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClaimRepositoryImpl @Inject constructor(
    private val claimApi: ClaimApi,
    private val claimDao: ClaimDao,
    private val claimCommentDao: ClaimCommentDao
) : ClaimRepository {

    override fun getClaimsByStatus(
        coroutineScope: CoroutineScope,
        listStatuses: List<Claim.Status>
    ) = claimDao.getClaimsByStatus(
        listStatuses
    ).flowOn(Dispatchers.Default)

    override suspend fun refreshClaims() = makeRequest(
        request = { claimApi.getAllClaims() },
        onSuccess = { body ->
            val apiId = body
                .map { it.id }
            val databaseId = claimDao.getAllClaims()
                .map { it.claim.id }
                .toMutableList()
            databaseId.removeAll(apiId)
            claimDao.removeClaimsItemsByIdList(databaseId)
            claimDao.insertClaim(body.toEntity())
        }
    )

    override suspend fun modificationOfExistingClaim(editedClaim: Claim): Claim = makeRequest(
        request = { claimApi.updateClaim(editedClaim) },
        onSuccess = { body ->
            claimDao.insertClaim(body.toEntity())
            body
        }
    )

    override suspend fun createNewClaim(claim: Claim): Claim = makeRequest(
        request = { claimApi.saveClaim(claim) },
        onSuccess = { body ->
            claimDao.insertClaim(body.toEntity())
            body
        }
    )

    override fun getClaimById(id: Int) = claimDao.getClaimById(id)

    override suspend fun getAllCommentsForClaim(id: Int): List<ClaimComment> = makeRequest(
        request = { claimApi.getAllClaimComments(id) },
        onSuccess = { body ->
            claimCommentDao.insertComments(body.toEntity())
            body
        }
    )

    override suspend fun saveClaimComment(claimId: Int, comment: ClaimComment): ClaimComment =
        makeRequest(
            request = { claimApi.saveClaimComment(claimId, comment) },
            onSuccess = { body ->
                claimCommentDao.insertComment(body.toEntity())
                body
            }
        )

    override suspend fun changeClaimStatus(
        claimId: Int,
        newStatus: Claim.Status,
        executorId: Int?,
        claimComment: ClaimComment
    ): Claim =
        makeRequest(
            request = {
                claimApi.updateClaimStatus(
                    claimId,
                    newStatus,
                    executorId,
                    claimComment
                )
            },
            onSuccess = { body ->
                claimDao.insertClaim(body.toEntity())
                getAllCommentsForClaim(claimId)
                body
            }
        )

    override suspend fun changeClaimComment(comment: ClaimComment): ClaimComment = makeRequest(
        request = { claimApi.updateClaimComment(comment) },
        onSuccess = { body ->
            claimCommentDao.insertComment(body.toEntity())
            body
        }
    )

    override suspend fun getAllClaimsWithOpenAndInProgressStatus(): List<Claim> {
        return makeRequest(
            request = { claimApi.getClaimsInOpenAndInProgressStatus() },
            onSuccess = { body ->
                claimDao.insertClaim(body.toEntity())
                body
            }
        )
    }
}
