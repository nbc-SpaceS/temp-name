package com.wannabeinseoul.seoulpublicservice.pref

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/** 관심 지역을 저장하는 Repository. */
interface RegionPrefRepository {
    fun save(value: List<String>)
    fun load(): List<String>
    fun selectedRegion(): LiveData<String>
}

class RegionPrefRepositoryImpl(context: Context) : RegionPrefRepository {

    private val pref = context.getSharedPreferences("RegionPrefRepository", Context.MODE_PRIVATE)
    private val _selectedRegion = MutableLiveData<String>()

    init {
        _selectedRegion.value = load().firstOrNull() ?: ""
    }

    override fun save(value: List<String>) {
        clearData()

        value.forEach {
            pref.edit().putString(it, it).apply()
        }

        _selectedRegion.value = value.firstOrNull() ?: ""
    }

    override fun load(): List<String> = pref.all.map { it.value as String }

    override fun selectedRegion(): LiveData<String> = _selectedRegion

    private fun clearData() {
        pref.edit().clear().apply()
    }
}
