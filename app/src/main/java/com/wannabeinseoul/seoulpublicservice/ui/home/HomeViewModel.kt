package com.wannabeinseoul.seoulpublicservice.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel() : ViewModel() {
    val selectedRegion = MutableLiveData<String>()
}