package ru.iteco.fmhandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.iteco.fmhandroid.adapter.OnClaimCommentItemClickListener
import ru.iteco.fmhandroid.dto.Claim
import ru.iteco.fmhandroid.dto.ClaimComment
import ru.iteco.fmhandroid.dto.FullClaim
import ru.iteco.fmhandroid.dto.User
import ru.iteco.fmhandroid.repository.claimRepository.ClaimRepository
import ru.iteco.fmhandroid.repository.userRepository.UserRepository
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class ClaimCardViewModel @Inject constructor(
    private val claimRepository: ClaimRepository,
    private val userRepository: UserRepository
) : ViewModel(), OnClaimCommentItemClickListener {

    private var claimId by Delegates.notNull<Int>()

    val dataFullClaim: Flow<FullClaim> by lazy {
        claimRepository.getClaimById(claimId)
    }

    val currentUser: User
        get() = userRepository.currentUser

    val userList: List<User>
        get() = userRepository.userList

    val openClaimCommentEvent = MutableSharedFlow<ClaimComment>()
    private val claimStatusChangedEvent = MutableSharedFlow<Unit>()
    val claimStatusChangeExceptionEvent = MutableSharedFlow<Unit>()
    val claimUpdateExceptionEvent = MutableSharedFlow<Unit>()
    val claimUpdatedEvent = MutableSharedFlow<Unit>()
    val claimCreatedEvent = MutableSharedFlow<Unit>()
    val createClaimExceptionEvent = MutableSharedFlow<Unit>()
    val claimCommentCreatedEvent = MutableSharedFlow<Unit>()
    val claimCommentUpdatedEvent = MutableSharedFlow<Unit>()
    val claimCommentCreateExceptionEvent = MutableSharedFlow<Unit>()
    val updateClaimCommentExceptionEvent = MutableSharedFlow<Unit>()

    fun createClaimComment(claimComment: ClaimComment) {
        viewModelScope.launch {
            try {
                claimRepository.saveClaimComment(claimComment.claimId, claimComment)
                claimCommentCreatedEvent.emit(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                claimCommentCreateExceptionEvent.emit(Unit)
            }
        }
    }

    fun updateClaimComment(comment: ClaimComment) {
        viewModelScope.launch {
            try {
                claimRepository.changeClaimComment(comment)
                claimCommentUpdatedEvent.emit(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                updateClaimCommentExceptionEvent.emit(Unit)
            }
        }
    }

    fun save(claim: Claim) {
        viewModelScope.launch {
            try {
                claimRepository.createNewClaim(claim)
                claimCreatedEvent.emit(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                createClaimExceptionEvent.emit(Unit)
            }
        }
    }

    fun updateClaim(updatedClaim: Claim) {
        viewModelScope.launch {
            try {
                claimRepository.modificationOfExistingClaim(updatedClaim)
                claimUpdatedEvent.emit(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                claimUpdateExceptionEvent.emit(Unit)
            }
        }
    }

    fun changeClaimStatus(
        claimId: Int,
        newClaimStatus: Claim.Status,
        executorId: Int?,
        claimComment: ClaimComment
    ) {
        viewModelScope.launch {
            try {
                claimRepository.changeClaimStatus(
                    claimId,
                    newClaimStatus,
                    executorId,
                    claimComment
                )
                claimStatusChangedEvent.emit(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                claimStatusChangeExceptionEvent.emit(Unit)
            }
        }
    }

    fun init(claimId: Int) {
        this.claimId = claimId
    }

    // region OnClaimCommentItemClickListener
    override fun onCard(claimComment: ClaimComment) {
        viewModelScope.launch {
            openClaimCommentEvent.emit(claimComment)
        }
    }
    // endregion OnClaimCommentItemClickListener
}
