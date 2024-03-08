package com.wannabeinseoul.seoulpublicservice.dialog.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databases.firebase.ComplaintRepository
import com.wannabeinseoul.seoulpublicservice.databases.firebase.ReviewEntity
import com.wannabeinseoul.seoulpublicservice.databases.firebase.ReviewRepository
import com.wannabeinseoul.seoulpublicservice.databases.firebase.ServiceRepository
import com.wannabeinseoul.seoulpublicservice.databases.firebase.UserBanRepository
import com.wannabeinseoul.seoulpublicservice.databases.firebase.UserRepository
import com.wannabeinseoul.seoulpublicservice.pref.IdPrefRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReviewViewModel(
    private val idPrefRepository: IdPrefRepository,
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
    private val serviceRepository: ServiceRepository,
    private val userBanRepository: UserBanRepository
) : ViewModel() {

    private val _uiState: MutableLiveData<List<ReviewItem>> = MutableLiveData()
    val uiState: LiveData<List<ReviewItem>> get() = _uiState

    private val _reviewCredentials: MutableLiveData<Boolean> = MutableLiveData()
    val reviewCredentials: LiveData<Boolean> get() = _reviewCredentials

    private val _isComplaintSelf: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()
    val isComplaintSelf: LiveData<Pair<Boolean, String>> get() = _isComplaintSelf

    fun uploadReview(svcId: String, review: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = idPrefRepository.load()
            val reviewId = id.takeLast(4) + System.currentTimeMillis().toString()

            userRepository.addUserReview(id, reviewId)
            serviceRepository.addServiceReview(svcId, reviewId)
            reviewRepository.addReview(
                reviewId,
                ReviewEntity(
                    userId = id,
                    svcId = svcId,
                    uploadTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    content = review
                )
            )

            _reviewCredentials.postValue(false)
            setReviews(svcId)
        }
    }

    fun setReviews(svcId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = serviceRepository.getServiceReviews(svcId)
            val banList = userBanRepository.getBanList().toMutableList().apply {
                remove(idPrefRepository.load())
            }

            _uiState.postValue(data.filter { it.userId !in banList })
        }
    }

    fun reviseReview(svcId: String, review: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = idPrefRepository.load()

            reviewRepository.reviseReview(id, svcId, review)

            setReviews(svcId)
        }
    }

    fun checkWritableUser(svcId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = idPrefRepository.load()

            _reviewCredentials.postValue(reviewRepository.checkCredentials(id, svcId))
        }
    }

    fun checkComplaintSelf(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = userRepository.getUserId(name)

            if (id == idPrefRepository.load()) {
                _isComplaintSelf.postValue(Pair(true, name))
            } else {
                _isComplaintSelf.postValue(Pair(false, name))
            }
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
                    idPrefRepository = container.idPrefRepository,
                    reviewRepository = container.reviewRepository,
                    userRepository = container.userRepository,
                    serviceRepository = container.serviceRepository,
                    userBanRepository = container.userBanRepository
                )
            }
        }
    }
}