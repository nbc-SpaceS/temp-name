package com.example.seoulpublicservice.pref

import android.content.Context
import android.util.Log
import com.google.gson.Gson

interface SavedPrefRepository {
    fun saveSvcidList(svcidList: List<String>)
    fun loadSvcidList(): List<String>
    fun saveSvcid(svcid: String)
    fun clear()
    fun remove(svcid: String)
}

class SavedPrefRepositoryImpl(context: Context) : SavedPrefRepository {

    private val pref = context.getSharedPreferences("SavedPrefRepository", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val keySvcidList = "keySvcidList"

    private fun save(list: List<String>) {
        val json = gson.toJson(list)
        pref.edit().putString(keySvcidList, json).apply()
        Log.d("jj-SavedPrefRepositoryImpl", "save json: ${json.take(255)}")
    }

    override fun loadSvcidList(): List<String> {
        val json = pref.getString(keySvcidList, null) ?: return emptyList<String>()
            .apply { Log.w("jj-SavedPrefRepositoryImpl", "loadSvcidList got null") }
        Log.d("jj-SavedPrefRepositoryImpl", "loadSvcidList json: ${json.take(255)}")
        return gson.fromJson(json, Array<String>::class.java).toList()
    }

    override fun saveSvcidList(svcidList: List<String>) = save(loadSvcidList() + svcidList)

    override fun saveSvcid(svcid: String) = save(loadSvcidList() + svcid)

    override fun clear() = pref.edit().clear().apply()

    override fun remove(svcid: String) = save(loadSvcidList() - svcid)

}
