package com.wannabeinseoul.seoulpublicservice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private var selectedServiceId: String = ""

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
}