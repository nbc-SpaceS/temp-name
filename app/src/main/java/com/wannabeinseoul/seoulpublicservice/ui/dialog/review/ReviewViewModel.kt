package com.wannabeinseoul.seoulpublicservice.ui.dialog.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.pref.IdPrefRepository
import com.wannabeinseoul.seoulpublicservice.ui.dialog.complaint.ComplaintUserInfo
import com.wannabeinseoul.seoulpublicservice.usecase.CheckComplaintSelfUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.CheckCredentialsUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.DeleteReviewUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.GetReviewListUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.ReviseReviewUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.UploadReviewUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val uploadReviewUseCase: UploadReviewUseCase,
    private val getReviewListUseCase: GetReviewListUseCase,
    private val reviseReviewUseCase: ReviseReviewUseCase,
    private val checkCredentialsUseCase: CheckCredentialsUseCase,
    private val checkComplaintSelfUseCase: CheckComplaintSelfUseCase,
    private val deleteReviewUseCase: DeleteReviewUseCase,
    private val idPrefRepository: IdPrefRepository,
    private val userName: String?,
    private val userProfileImage: String?
) : ViewModel() {

    private var _userId: String = idPrefRepository.load()
    val userId: String get() = _userId

    private val _uiState: MutableLiveData<List<ReviewItem>> = MutableLiveData()
    val uiState: LiveData<List<ReviewItem>> get() = _uiState

    private val _reviewCredentials: MutableLiveData<Boolean> = MutableLiveData()
    val reviewCredentials: LiveData<Boolean> get() = _reviewCredentials

    private val _isComplaintSelf: MutableLiveData<Pair<Boolean, ComplaintUserInfo>> =
        MutableLiveData()
    val isComplaintSelf: LiveData<Pair<Boolean, ComplaintUserInfo>> get() = _isComplaintSelf

    fun uploadReview(svcId: String, review: String) {
        _reviewCredentials.value = false
        _uiState.postValue(uiState.value.orEmpty().toMutableList().apply {
            add(
                0, ReviewItem(
                    "",
                    userId,
                    userName ?: "",
                    "",
                    review,
                    "#000000",
                    userProfileImage ?: ""
                )
            )
        })

        viewModelScope.launch(Dispatchers.IO) {
            uploadReviewUseCase(svcId, review)
            _uiState.postValue(getReviewListUseCase(svcId))
        }
    }

    fun setReviews(svcId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.postValue(getReviewListUseCase(svcId))
        }
    }

    fun setReviewsByList(list: List<ReviewItem>) {
        _uiState.value = list
    }

    fun reviseReview(svcId: String, review: String) {
        val reviewItem = uiState.value.orEmpty().toMutableList().withIndex()
            .find { it.value.userId == userId }

        if (reviewItem?.value?.reviewId != "") {
            val idx = reviewItem?.index
            if (idx != null) {
                val item = uiState.value?.get(idx)?.copy(content = review)
                _uiState.value = uiState.value.orEmpty().toMutableList().apply {
                    removeAt(idx)
                    add(idx, item!!)
                }
            }

            viewModelScope.launch(Dispatchers.IO) {
                reviseReviewUseCase(reviewItem?.value?.reviewId ?: "", review)
                _uiState.postValue(getReviewListUseCase(svcId))
            }
        }
    }

    fun deleteReview(svcId: String, reviewId: String) {
        val idx = uiState.value.orEmpty().toMutableList().withIndex()
            .find { it.value.reviewId == reviewId }?.index
        if (idx != null) {
            _uiState.value = uiState.value.orEmpty().toMutableList().apply {
                removeAt(idx)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            deleteReviewUseCase(svcId, reviewId)
            _uiState.postValue(getReviewListUseCase(svcId))
        }
    }

    fun checkWritableUser(svcId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _reviewCredentials.postValue(checkCredentialsUseCase(svcId))
        }
    }

    fun checkComplaintSelf(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isComplaintSelf.postValue(checkComplaintSelfUseCase(id))
        }
    }

    companion object {
        /** 뷰모델팩토리에서 의존성주입을 해준다 */
        val factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SeoulPublicServiceApplication)
                val container = application.container
                ReviewViewModel(
                    uploadReviewUseCase = container.uploadReviewUseCase,
                    getReviewListUseCase = container.getReviewListUseCase,
                    reviseReviewUseCase = container.reviseReviewUseCase,
                    checkCredentialsUseCase = container.checkCredentialsUseCase,
                    checkComplaintSelfUseCase = container.checkComplaintSelfUseCase,
                    deleteReviewUseCase = container.deleteReviewUseCase,
                    idPrefRepository = container.idPrefRepository,
                    userName = application.userName.value,
                    userProfileImage = application.userProfileImageUrl
                )
            }
        }
    }
}