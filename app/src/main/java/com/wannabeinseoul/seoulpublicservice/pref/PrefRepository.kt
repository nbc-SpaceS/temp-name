package com.wannabeinseoul.seoulpublicservice.pref

import android.content.Context
import android.util.Log

private const val JJTAG = "jj-PrefRepository"

/** 그냥 String으로 key, value 넣어서 사용할 수 있는 Repository.
 * 별로 권장하지는 않습니다. 용도에 따라 Repository를 만들어 사용하는 걸 추천. */
interface PrefRepository {
    fun save(key: String, value: String?)
    fun load(key: String): String?
}

class PrefRepositoryImpl(context: Context) : PrefRepository {

    private val pref = context.getSharedPreferences("DefaultPrefRepository", Context.MODE_PRIVATE)

    override fun save(key: String, value: String?) = pref.edit().putString(key, value).apply()

    override fun load(key: String) = pref.getString(key, null)
        .also { Log.d(JJTAG, "load $key: ${it?.take(255)}") }

}
