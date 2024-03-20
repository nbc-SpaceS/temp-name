package com.wannabeinseoul.seoulpublicservice.pref

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson

/** 관심 지역을 저장하는 Repository. */
interface RegionPrefRepository {
    fun save(value: List<String>)
    fun load(): List<String>
    fun loadSelectedRegion(): String
    fun saveSelectedRegion(num: Int)
    val selectedRegion: LiveData<String>
}

class RegionPrefRepositoryImpl(context: Context) : RegionPrefRepository {

    private val pref = context.getSharedPreferences("RegionPrefRepository", Context.MODE_PRIVATE)
    private var selectedRegionIndex: Int = -1
    private val gson = Gson()

    private val _selectedRegion = MutableLiveData<String>()
    override val selectedRegion: LiveData<String> get() = _selectedRegion


    override fun save(value: List<String>) {
        clearData()
        val json = gson.toJson(value)
        pref.edit().putString("selectedRegion", json).apply()
    }

    override fun load(): List<String> {
        val json = pref.getString("selectedRegion", null) ?: return emptyList()
        return gson.fromJson(json, Array<String>::class.java).toList()
    }

    override fun loadSelectedRegion(): String =
        if (selectedRegionIndex == -1) "지역선택" else load()[selectedRegionIndex]

    override fun saveSelectedRegion(num: Int) {
        selectedRegionIndex = num - 1
    }

    private fun clearData() {
        pref.edit().clear().apply()
    }
}
