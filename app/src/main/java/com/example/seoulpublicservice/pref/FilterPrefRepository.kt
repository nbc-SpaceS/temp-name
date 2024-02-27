package com.example.seoulpublicservice.pref

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.chip.Chip
import com.google.gson.GsonBuilder

/** 관심 지역을 저장하는 Repository. */
interface FilterPrefRepository {
    fun save(
        value: List<List<String>>
    )
    fun load(): List<List<String>>
    fun clearData(): Unit
}

class FilterPrefRepositoryImpl(context: Context) : FilterPrefRepository {

    private val pref = context.getSharedPreferences("FilterPrefRepository", Context.MODE_PRIVATE)

    override fun save(
        value: List<List<String>>
    ) {
        clearData()

        val gsonBuilder = GsonBuilder()
        val editor = pref.edit()
        val title1Header1Data = gsonBuilder.create().toJson(value[0])
        val title1Header2Data = gsonBuilder.create().toJson(value[1])
        val title1Header3Data = gsonBuilder.create().toJson(value[2])
        val title1Header4Data = gsonBuilder.create().toJson(value[3])
        val title1Header5Data = gsonBuilder.create().toJson(value[4])
        val title2Header1Data = gsonBuilder.create().toJson(value[5])
        val title3Header1Data = gsonBuilder.create().toJson(value[6])
        val title4Header1Data = gsonBuilder.create().toJson(value[7])
        editor.putString("체육시설", title1Header1Data)
        editor.putString("교육", title1Header2Data)
        editor.putString("문화행사", title1Header3Data)
        editor.putString("시설대관", title1Header4Data)
        editor.putString("진료", title1Header5Data)
        editor.putString("관심지역", title2Header1Data)
        editor.putString("접수가능여부", title3Header1Data)
        editor.putString("요금", title4Header1Data)
        editor.apply()
    }

    override fun load(): List<List<String>> {
        val title1Header1Data = pref.getString("체육시설", "")
        val title1Header2Data = pref.getString("교육", "")
        val title1Header3Data = pref.getString("문화행사", "")
        val title1Header4Data = pref.getString("시설대관", "")
        val title1Header5Data = pref.getString("진료", "")
        val title2Header1Data = pref.getString("관심지역", "")
        val title3Header1Data = pref.getString("접수가능여부", "")
        val title4Header1Data = pref.getString("요금", "")

        val loadedData = mutableListOf<List<String>>()

        if (title1Header1Data != "") {
            val gsonBuilder = GsonBuilder()
            val list = gsonBuilder.create().fromJson(title1Header1Data, List::class.java)
            loadedData.add(list as List<String>)
        } else {
            loadedData.add(listOf())
        }

        if (title1Header2Data != "") {
            val gsonBuilder = GsonBuilder()
            val list = gsonBuilder.create().fromJson(title1Header2Data, List::class.java)
            loadedData.add(list as List<String>)
        } else {
            loadedData.add(listOf())
        }

        if (title1Header3Data != "") {
            val gsonBuilder = GsonBuilder()
            val list = gsonBuilder.create().fromJson(title1Header3Data, List::class.java)
            loadedData.add(list as List<String>)
        } else {
            loadedData.add(listOf())
        }

        if (title1Header4Data != "") {
            val gsonBuilder = GsonBuilder()
            val list = gsonBuilder.create().fromJson(title1Header4Data, List::class.java)
            loadedData.add(list as List<String>)
        } else {
            loadedData.add(listOf())
        }

        if (title1Header5Data != "") {
            val gsonBuilder = GsonBuilder()
            val list = gsonBuilder.create().fromJson(title1Header5Data, List::class.java)
            loadedData.add(list as List<String>)
        } else {
            loadedData.add(listOf())
        }

        if (title2Header1Data != "") {
            val gsonBuilder = GsonBuilder()
            val list = gsonBuilder.create().fromJson(title2Header1Data, List::class.java)
            loadedData.add(list as List<String>)
        } else {
            loadedData.add(listOf())
        }

        if (title3Header1Data != "") {
            val gsonBuilder = GsonBuilder()
            val list = gsonBuilder.create().fromJson(title3Header1Data, List::class.java)
            loadedData.add(list as List<String>)
        } else {
            loadedData.add(listOf())
        }

        if (title4Header1Data != "") {
            val gsonBuilder = GsonBuilder()
            val list = gsonBuilder.create().fromJson(title4Header1Data, List::class.java)
            loadedData.add(list as List<String>)
        } else {
            loadedData.add(listOf())
        }

        return loadedData
    }

    override fun clearData() {
        pref.edit().clear().apply()
    }
}
