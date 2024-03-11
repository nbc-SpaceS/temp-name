package com.wannabeinseoul.seoulpublicservice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wannabeinseoul.seoulpublicservice.dialog.review.ReviewItem

class MainViewModel: ViewModel() {

    private var selectedServiceId: String = ""
    private var currentReviewList: List<ReviewItem> = listOf()

    private val _applyFilter: MutableLiveData<Boolean> = MutableLiveData()
    val applyFilter: LiveData<Boolean> get() = _applyFilter

    private val _refreshReviewListState: MutableLiveData<Boolean> = MutableLiveData()
    val refreshReviewListState: LiveData<Boolean> get() = _refreshReviewListState

    fun setFilterState(flag: Boolean) {
        _applyFilter.value = flag
    }

    fun setServiceId(id: String) {
        selectedServiceId = id
    }

    fun getServiceId() = selectedServiceId

    fun setReviewListState(flag: Boolean) {
        _refreshReviewListState.value = flag
    }

    fun setCurrentReviewList(list : List<ReviewItem>) {
        currentReviewList = list
    }

    fun getCurrentReviewList(): List<ReviewItem> = currentReviewList
}