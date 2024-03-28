package com.wannabeinseoul.seoulpublicservice.pref

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wannabeinseoul.seoulpublicservice.databases.RecentEntity
import com.wannabeinseoul.seoulpublicservice.util.DLog

interface RecentPrefRepository {
    fun setRecent(value: RecentEntity)
    fun getRecent(): List<RecentEntity>
}

class RecentPrefRepositoryImpl(context: Context) : RecentPrefRepository {
    private val pref = context.getSharedPreferences("RecentPrefRepository", Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun setRecent(value: RecentEntity) {
        val editor = pref.edit()
        val temp = mutableListOf<RecentEntity>()
        val recent = getRecent().toMutableList()
        if (recent.isEmpty()) {
            temp.add(value)
            editor.putString(value.SVCID, gson.toJson(temp))
            temp.remove(value)
        }

        val index = recent.find { it.SVCID == value.SVCID } // 없으면 널인듯
        DLog.i("This is RecentPrefRepository", "recent : $recent\nindex : $index")

        if (index != null) {
            recent.remove(index)
        } else {
            if (recent.size >= 10) {
                val oldItem = recent.minByOrNull { it.DATETIME }
                DLog.i(
                    "This is RecentPrefRepository",
                    "oldItem : $oldItem\nrecent size : ${recent.size}\noldDateTime : ${oldItem?.DATETIME}"
                )
                editor.remove(oldItem!!.SVCID)
            }
        }
        temp.add(value)
        editor.putString(value.SVCID, gson.toJson(temp))
        editor.apply()
        temp.remove(value)
    }

    override fun getRecent(): List<RecentEntity> {
        val recent = mutableListOf<RecentEntity>()
        pref.all.forEach { (_, value) ->
            DLog.i(
                "This is RecentPrefRepository",
                "value : ${value}\nall value size : ${pref.all.values.size}"
            )
            val json = value as String
            json.let {
                val list = gson.fromJson<List<RecentEntity>>(
                    json,
                    object : TypeToken<List<RecentEntity>>() {}.type
                )
                recent.addAll(list)
            }
        }
        return recent.sortedByDescending { it.DATETIME }
    }
}