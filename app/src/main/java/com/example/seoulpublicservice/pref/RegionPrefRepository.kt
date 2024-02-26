package com.example.seoulpublicservice.pref

import android.content.Context

/** 관심 지역을 저장하는 Repository. */
interface RegionPrefRepository {
    fun save(value: List<String>)
    fun load(): List<String>
}

class RegionPrefRepositoryImpl(context: Context) : RegionPrefRepository {

    private val pref = context.getSharedPreferences("RegionPrefRepository", Context.MODE_PRIVATE)

    override fun save(value: List<String>) {
        clearData()

        value.forEach {
            pref.edit().putString(it, it).apply()
        }
    }

    override fun load(): List<String> = pref.all.map { it.value as String }

    private fun clearData() {
        pref.edit().clear().apply()
    }
}
