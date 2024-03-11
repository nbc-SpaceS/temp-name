package com.wannabeinseoul.seoulpublicservice.ui.dialog.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databases.firebase.ReviewRepository
import com.wannabeinseoul.seoulpublicservice.databases.firebase.ServiceRepository
import com.wannabeinseoul.seoulpublicservice.databases.firebase.UserBanRepository
import com.wannabeinseoul.seoulpublicservice.databases.firebase.UserRepository
import com.wannabeinseoul.seoulpublicservice.ui.dialog.complaint.ComplaintUserInfo
import com.wannabeinseoul.seoulpublicservice.pref.IdPrefRepository
import com.wannabeinseoul.seoulpublicservice.usecase.CheckComplaintSelfUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.CheckCredentialsUseCase
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
    private val checkComplaintSelfUseCase: CheckComplaintSelfUseCase
) : ViewModel() {

    private val _uiState: MutableLiveData<List<ReviewItem>> = MutableLiveData()
    val uiState: LiveData<List<ReviewItem>> get() = _uiState

    private val _reviewCredentials: MutableLiveData<Boolean> = MutableLiveData()
    val reviewCredentials: LiveData<Boolean> get() = _reviewCredentials

    private val _isComplaintSelf: MutableLiveData<Pair<Boolean, ComplaintUserInfo>> = MutableLiveData()
    val isComplaintSelf: LiveData<Pair<Boolean, ComplaintUserInfo>> get() = _isComplaintSelf

    fun uploadReview(svcId: String, review: String) {
        viewModelScope.launch(Dispatchers.IO) {
            uploadReviewUseCase(svcId, review)
            _reviewCredentials.postValue(false)
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
        viewModelScope.launch(Dispatchers.IO) {
            reviseReviewUseCase(svcId, review)
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
                    checkComplaintSelfUseCase = container.checkComplaintSelfUseCase
                )
            }
        }
    }
}