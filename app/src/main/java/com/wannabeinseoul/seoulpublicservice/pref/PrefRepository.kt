package com.wannabeinseoul.seoulpublicservice.pref

import android.content.Context
import android.util.Log

/** 그냥 String으로 key, value 넣어서 사용할 수 있는 Repository.
 * 별로 권장하지는 않습니다. 용도에 따라 Repository를 만들어 사용하는 걸 추천. */
interface PrefRepository {
    fun save(key: String, value: String)
    fun load(key: String): String
}

class PrefRepositoryImpl(context: Context) : PrefRepository {

    private val pref = context.getSharedPreferences("DefaultPrefRepository", Context.MODE_PRIVATE)

    override fun save(key: String, value: String) {
        pref.edit().putString(key, value).apply()
    }

    override fun load(key: String): String {
        val loaded = pref.getString(key, null) ?: return ""
            .apply { Log.w("jj-PrefRepositoryImpl", "load $key got null") }

        val s = if (loaded.length <= 63) loaded else loaded.substring(0, 63)
        Log.d("jj-PrefRepositoryImpl", "loaded $key: $s")

        return loaded
    }

}
