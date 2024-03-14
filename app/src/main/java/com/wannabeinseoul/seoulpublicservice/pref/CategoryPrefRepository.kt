package com.wannabeinseoul.seoulpublicservice.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface CategoryPrefRepository {
    fun save(value: List<String>)
    fun load(): List<String>
    fun saveSelectedCategory(category: String)
    val selectedCategory: LiveData<String>
}



class CategoryPrefRepositoryImpl(context: Context) : CategoryPrefRepository {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("recommend_prefs", Context.MODE_PRIVATE)
    private val KEY_RECOMMENDATIONS = "recommendations"
    private val _selectedCategory = MutableLiveData<String>()
    override val selectedCategory: LiveData<String> get() = _selectedCategory

    override fun save(value: List<String>) {
        // Save logic here
    }
    override fun load(): List<String> {
        return sharedPreferences.getString(KEY_RECOMMENDATIONS, "")?.split(",") ?: emptyList()
    }

    override fun saveSelectedCategory(category: String) {
        _selectedCategory.value = category
    }
}