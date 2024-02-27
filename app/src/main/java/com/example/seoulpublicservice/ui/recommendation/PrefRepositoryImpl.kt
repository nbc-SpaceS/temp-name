package com.example.seoulpublicservice.ui.recommendation

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.seoulpublicservice.pref.PrefRepository

class PrefRepositoryImpl(context: Context) : PrefRepository {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("recommend_shared_prefs", Context.MODE_PRIVATE)

    override fun save(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun load(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }
}