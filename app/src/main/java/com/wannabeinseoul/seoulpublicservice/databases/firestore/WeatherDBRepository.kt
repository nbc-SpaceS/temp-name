package com.wannabeinseoul.seoulpublicservice.databases.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wannabeinseoul.seoulpublicservice.weather.WeatherShort
import kotlinx.coroutines.tasks.await

interface WeatherDBRepository {
    suspend fun getWeather(region: String): List<WeatherShort>?

    suspend fun getUpdateTime(region: String): Long?
    suspend fun setWeather(region: String, weatherList: List<WeatherShort>)
}

class WeatherDBRepositoryImpl : WeatherDBRepository {
    private val fireStore = Firebase.firestore

    override suspend fun getWeather(region: String): List<WeatherShort>? {
        val weather = fireStore.collection("weather").document(region).get().await()

        return if (weather.exists()) {
            val list =
                fireStore.collection("weather").document(region).collection("day").get().await()
                    .toObjects(WeatherShort::class.java).toMutableList()
            list.add(list.removeAt(2))
            list
        } else {
            null
        }
    }

    override suspend fun getUpdateTime(region: String): Long? {
        val weather = fireStore.collection("weather").document(region).get().await()

        return if (weather.exists()) {
            weather.data?.get("updateTime") as Long
        } else {
            null
        }
    }

    override suspend fun setWeather(region: String, weatherList: List<WeatherShort>) {
        val updateTime = getUpdateTime(region)
        val currentTime = System.currentTimeMillis()

        if (updateTime == null) {
            fireStore.collection("weather").document(region)
                .set(hashMapOf("updateTime" to System.currentTimeMillis()))

            weatherList.forEachIndexed { index, it ->
                fireStore.collection("weather").document(region).collection("day")
                    .document("$index").set(it)
            }
        } else {
            if (currentTime - updateTime > 3600000) fireStore.collection("weather").document(region)
                .set(hashMapOf("updateTime" to System.currentTimeMillis()))

            weatherList.forEachIndexed { index, it ->
                fireStore.collection("weather").document(region).collection("day")
                    .document("$index").set(it)
            }
        }

    }
}