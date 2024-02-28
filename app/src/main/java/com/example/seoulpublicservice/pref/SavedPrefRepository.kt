package com.example.seoulpublicservice.pref

import android.content.Context
import android.util.Log
import com.google.gson.Gson

interface SavedPrefRepository {
    fun saveServices(svcidList: List<String>)
    fun loadServices(): List<String>
}

class SavedPrefRepositoryImpl(context: Context) : SavedPrefRepository {

    private val pref = context.getSharedPreferences("SavedPrefRepository", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val keySavedServiceIdList = "keySavedServiceIdList"

    override fun saveServices(svcidList: List<String>) {
        val json = gson.toJson(svcidList)
        pref.edit().putString(keySavedServiceIdList, json).apply()
    }

    override fun loadServices(): List<String> {
        val json = pref.getString(keySavedServiceIdList, null) ?: return emptyList<String>()
            .apply { Log.w("jj-SavedPrefRepositoryImpl", "loadServices got null") }
        Log.d("jj-SavedPrefRepositoryImpl", "loadServices json: ${json.take(255)}")
        return gson.fromJson(json, Array<String>::class.java).toList()
    }

}
