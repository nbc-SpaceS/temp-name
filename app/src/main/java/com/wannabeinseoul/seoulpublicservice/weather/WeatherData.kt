package com.wannabeinseoul.seoulpublicservice.weather

import android.util.Log

private const val TAG = "WeatherData"
object WeatherData {
    private var weatherShort: List<Item>? = null
    private var weatherMid: com.wannabeinseoul.seoulpublicservice.kma.midLandFcst.Item? = null
    private var weatherMidTmp: com.wannabeinseoul.seoulpublicservice.kma.midTemp.Item? = null
    private var weatherMix: List<WeatherShort>? = null

    private var weatherArea: String? = null
    private var weatherDate: Int? = null

    fun saveShort(w: List<Item>) {
        weatherShort = w
        Log.i(TAG,"saveShort :\nw : ${w.take(20)}\nweatherShort = ${weatherShort?.take(20)}")
    }
    fun getShort(): List<Item>? {
        Log.i(TAG,"getShort :\nweatherShort = ${weatherShort?.take(20)}")
        return weatherShort
    }
    fun saveMid(w: com.wannabeinseoul.seoulpublicservice.kma.midLandFcst.Item) {
        weatherMid = w
        Log.i(TAG,"saveMid :\nw : $w\nweatherMid = $weatherMid")
    }
    fun getMid(): com.wannabeinseoul.seoulpublicservice.kma.midLandFcst.Item? {
        Log.i(TAG,"getMid :\nweatherMid = $weatherMid")
        return weatherMid
    }
    fun saveTmp(w: com.wannabeinseoul.seoulpublicservice.kma.midTemp.Item) {
        weatherMidTmp = w
        Log.i(TAG,"saveTmp :\nw : $w\nweatherMidTmp = $weatherMidTmp")
    }
    fun getTmp(): com.wannabeinseoul.seoulpublicservice.kma.midTemp.Item? {
        Log.i(TAG,"getTmp :\nweatherMidTmp = $weatherMidTmp")
        return weatherMidTmp
    }

    fun saveMix(mix: List<WeatherShort>) {
        weatherMix = mix
        Log.i(TAG,"saveMix :\nmix = $weatherMix")
    }
    fun getMix(): List<WeatherShort>? {
        Log.i(TAG,"getMix :\nweatherMix = $weatherMix")
        return weatherMix
    }

    fun saveAreaDate(area: String, date: Int) {
        weatherArea = area
        weatherDate = date
        Log.i(TAG,"saveAreaDate :\narea = $area\ndate = $date")
    }

    fun getArea(): String? {
        Log.i(TAG,"getArea :\nweatherArea = $weatherArea")
        return weatherArea
    }
    fun getDate(): Int? {
        Log.i(TAG,"getDate :\nweatherDate = $weatherDate")
        return weatherDate
    }
}