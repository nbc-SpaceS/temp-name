package com.wannabeinseoul.seoulpublicservice.pref

import android.content.Context
import com.google.gson.Gson
import com.wannabeinseoul.seoulpublicservice.seoul.Row

interface RowPrefRepository {
    fun saveRows(rowList: List<Row>)
    fun loadRows(): List<Row>
}

class RowPrefRepositoryImpl(context: Context) : RowPrefRepository {

    private val pref = context.getSharedPreferences("RowList", Context.MODE_PRIVATE)
    private val gson = Gson()

    private data object Key {
        const val rows = "rows"
    }

    override fun saveRows(rowList: List<Row>) {
        // TODO: 약 1600개 저장하다가 메모리 터짐. 한 512개씩 나눠서 분할 저장해야 할 듯. 그보다 Room으로.

//        val json = gson.toJson(rowList)
//        pref.edit().putString(Key.rows, json).apply()
    }

    override fun loadRows(): List<Row> {
//        val json = pref.getString(Key.rows, null) ?: return emptyList<Row>()
//            .apply { Log.w("jj-RowPrefRepositoryImpl", "loadRows got null") }
//
//        val s = if (json.length <= 63) json else json.substring(0, 63)
//        Log.d("jj-RowPrefRepositoryImpl", "loadRows json: $s")
//
//        return gson.fromJson(json, Array<Row>::class.java).toList()

        return emptyList()
    }

}
