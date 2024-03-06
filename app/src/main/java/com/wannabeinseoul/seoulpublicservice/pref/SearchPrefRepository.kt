package com.wannabeinseoul.seoulpublicservice.pref

import android.content.Context

/** 검색어를 저장하는 Repository */
interface SearchPrefRepository {
    fun save(value: String)
    fun load(): List<String>
}

class SearchPrefRepositoryImpl(context: Context) : SearchPrefRepository {

    private val pref = context.getSharedPreferences("SearchPrefRepository", Context.MODE_PRIVATE)

    override fun save(value: String) {
        val searches = load().toMutableList()
        searches.remove(value)  // 중복된 검색어가 있으면 제거
        searches.add(0, value)  // 최근 검색어를 맨 앞에 추가
        if (searches.size > 10) {
            searches.removeAt(10)  // 최근 검색어가 10개를 넘어가면 가장 오래된 검색어를 제거
        }
        pref.edit().putStringSet("searches", searches.toSet()).apply()
    }

    override fun load(): List<String> = pref.getStringSet("searches", emptySet())?.toList() ?: emptyList()
}