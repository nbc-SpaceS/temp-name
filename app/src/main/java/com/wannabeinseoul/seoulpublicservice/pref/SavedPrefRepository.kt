package com.wannabeinseoul.seoulpublicservice.pref

import android.content.Context
import android.util.Log
import com.google.gson.Gson

interface SavedPrefRepository {
    fun getSvcidList(): List<String>
    fun setSvcidList(list: List<String>)
    fun clear()
    fun addSvcidList(svcidList: List<String>)
    fun addSvcid(svcid: String)
    fun remove(svcid: String)
}

class SavedPrefRepositoryImpl(context: Context) : SavedPrefRepository {

    private val pref = context.getSharedPreferences("SavedPrefRepository", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val keySvcidList = "keySvcidList"

    // TODO: 저장 목록을 여기에 라이브데이터로 두고 마이페이지에서 바로 옵저빙 해버리면 어떨까??
    // init 하면서 pref 읽어놓고
    // 라이브데이터 옵저빙해서 pref에 쓰기
    // 모든 처리는 라이브데이터에 대해서만.

    override fun getSvcidList(): List<String> {
        val json = pref.getString(keySvcidList, null) ?: return emptyList<String>()
            .apply { Log.w("jj-SavedPrefRepositoryImpl", "loadSvcidList got null") }
        Log.d("jj-SavedPrefRepositoryImpl", "loadSvcidList json: ${json.take(255)}")
        return gson.fromJson(json, Array<String>::class.java).toList()
    }

    override fun setSvcidList(list: List<String>) {
        val json = gson.toJson(list)
        pref.edit().putString(keySvcidList, json).apply()
        Log.d("jj-SavedPrefRepositoryImpl", "save json: ${json.take(255)}")
    }

    override fun clear() = pref.edit().clear().apply()

    override fun addSvcidList(svcidList: List<String>) = setSvcidList(getSvcidList() + svcidList)

    override fun addSvcid(svcid: String) = setSvcidList(getSvcidList() + svcid)

    override fun remove(svcid: String) = setSvcidList(getSvcidList() - svcid)

}
