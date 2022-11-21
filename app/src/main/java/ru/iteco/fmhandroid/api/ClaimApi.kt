package ru.iteco.fmhandroid.api

import retrofit2.Response
import retrofit2.http.*
import ru.iteco.fmhandroid.dto.Claim
import ru.iteco.fmhandroid.dto.ClaimComment

interface ClaimApi {
    @GET("claims")
    suspend fun getAllClaims(): Response<List<Claim>>

    @GET("claims/open-in-progress")
    suspend fun getClaimsInOpenAndInProgressStatus(): Response<List<Claim>>

    @GET("claims/{id}/comments")
    suspend fun getAllClaimComments(@Path("id") id: Int): Response<List<ClaimComment>>

    @POST("claims")
    suspend fun saveClaim(@Body claim: Claim): Response<Claim>

    @POST("claims/{id}/comments")
    suspend fun saveClaimComment(
        @Path("id") id: Int,
        @Body claimComment: ClaimComment
    ): Response<ClaimComment>

    @PUT("claims")
    suspend fun updateClaim(@Body claim: Claim): Response<Claim>

    @PUT("claims/{id}/status")
    suspend fun updateClaimStatus(
        @Path("id") id: Int,
        @Query("status") claimStatus: Claim.Status,
        @Query("executorId") executorId: Int?,
        @Body claimComment: ClaimComment
    ): Response<Claim>

    @PUT("claims/comments")
    suspend fun updateClaimComment(@Body claimComment: ClaimComment): Response<ClaimComment>
}
