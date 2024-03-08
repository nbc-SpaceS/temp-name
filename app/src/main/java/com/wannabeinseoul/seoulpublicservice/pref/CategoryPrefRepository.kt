package com.wannabeinseoul.seoulpublicservice.pref

import android.content.Context
import android.content.SharedPreferences

interface CategoryPrefRepository {
    fun save(value: List<String>)
    fun load(): List<String>
}



class CategoryPrefRepositoryImpl(context: Context) : CategoryPrefRepository {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("recommend_prefs", Context.MODE_PRIVATE)
    private val KEY_RECOMMENDATIONS = "recommendations"

    override fun save(value: List<String>) {
        // Save logic here
    }
    override fun load(): List<String> {

        return sharedPreferences.getString(KEY_RECOMMENDATIONS, "")?.split(",") ?: emptyList()
    }
}