package com.wannabeinseoul.seoulpublicservice.weather

import android.util.Log

private const val TAG = "WeatherData"
object WeatherData {
    private var weatherShort: List<Item>? = null
    private var weatherMid: com.wannabeinseoul.seoulpublicservice.kma.midLandFcst.Item? = null
    private var weatherMidTmp: com.wannabeinseoul.seoulpublicservice.kma.midTemp.Item? = null

    fun saveShort(w: List<Item>) {
        weatherShort = w
        Log.i(TAG,"saveShort :\nw : ${w.take(20)}\nweatherShort = ${weatherShort?.take(20)}")
    }
    fun getShort(): List<Item>? {
        Log.i(TAG,"getShort :\nweatherShort = ${weatherShort?.take(20)}")
        return weatherShort
    }
    fun saveMid(w: com.wannabeinseoul.seoulpublicservice.kma.midLandFcst.Item) {
        Log.i(TAG,"saveMid :\nw : $w\nweatherMid = $weatherMid")
        weatherMid = w
    }
    fun getMid(): com.wannabeinseoul.seoulpublicservice.kma.midLandFcst.Item? {
        Log.i(TAG,"getMid :\nweatherMid = $weatherMid")
        return weatherMid
    }
    fun saveTmp(w: com.wannabeinseoul.seoulpublicservice.kma.midTemp.Item) {
        Log.i(TAG,"saveTmp :\nw : $w\nweatherMidTmp = $weatherMidTmp")
        weatherMidTmp = w
    }
    fun getTmp(): com.wannabeinseoul.seoulpublicservice.kma.midTemp.Item? {
        Log.i(TAG,"getTmp :\nweatherMidTmp = $weatherMidTmp")
        return weatherMidTmp
    }
}