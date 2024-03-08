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
}

class RegionPrefRepositoryImpl(context: Context) : RegionPrefRepository {

    private val pref = context.getSharedPreferences("RegionPrefRepository", Context.MODE_PRIVATE)
    private var selectedRegion: Int = -1
    private val gson = Gson()

    init {
//        _selectedRegion.value = load().firstOrNull() ?: ""
    }

    override fun save(value: List<String>) {
        clearData()

//        _selectedRegion.value = value.firstOrNull() ?: ""
    }

    override fun load(): List<String> {
        val json = pref.getString("selectedRegion", null) ?: return emptyList()
        return gson.fromJson(json, Array<String>::class.java).toList()
    }

    override fun loadSelectedRegion(): String = if (selectedRegion == -1) "지역선택" else load()[selectedRegion]

    override fun saveSelectedRegion(num: Int) {
        selectedRegion = num - 1
    }


    private fun clearData() {
        pref.edit().clear().apply()
    }
}
