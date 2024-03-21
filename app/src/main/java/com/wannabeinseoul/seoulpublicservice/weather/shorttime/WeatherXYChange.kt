package com.wannabeinseoul.seoulpublicservice.weather.shorttime

import android.util.Log

class WeatherXYChange{
    fun onChange(mode: Int, x: Double, y: Double) {
        val tmp = convertGRID_GPS(TO_GRID, 37.579871128849334, 126.98935225645432)
        val change = convertGRID_GPS(mode, x, y)
        Log.e(">>", "x = " + tmp.x + ", y = " + tmp.y)
        Log.i("This is TestActivity Embed in DetailFragment", "x = $x / ${change.x}, y = $y / ${change.y}")
    }

    /**
     * @property change 좌표 기상청용으로 변환하기
     * @param mode 0: 좌표 -> 기상청, 1: 기상청 -> 좌표
     * @param x 위도(-90 ~ 90): Double
     * @param y 경도(-180 ~ 180): Double
     * @return `Pair(x, y)`
     */

    fun change(mode: Int, x: Double, y: Double): Pair<Int, Int> {
        val change = convertGRID_GPS(mode, x, y)
        Log.i("This is TestActivity Embed in DetailFragment", "x = $x / ${change.x}, y = $y / ${change.y}")
        return Pair(change.x.toInt(), change.y.toInt())
    }

    /**
     * @property convertGRID_GPS 좌표와를 세부구역으로 변경하거나, 세부구역을 경위도로 변환
     * @param mode 0 : 좌표를 세부구역으로 변경, 1 : 세부구역을 좌표로 변경
     * @param lat_X 위도
     * @param lng_Y 경도
     * @return [LatXLngY]
     */

    private fun convertGRID_GPS(mode: Int, lat_X: Double, lng_Y: Double): LatXLngY {
        val RE = 6371.00877 // 지구 반경(km)
        val GRID = 5.0 // 격자 간격(km)
        val SLAT1 = 30.0 // 투영 위도1(degree)
        val SLAT2 = 60.0 // 투영 위도2(degree)
        val OLON = 126.0 // 기준점 경도(degree)
        val OLAT = 38.0 // 기준점 위도(degree)
        val XO = 43.0 // 기준점 X좌표(GRID)
        val YO = 136.0 // 기1준점 Y좌표(GRID)

        //
        // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )
        //
        val DEGRAD = Math.PI / 180.0
        val RADDEG = 180.0 / Math.PI
        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD
        var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn)
        var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn
        var ro = Math.tan(Math.PI * 0.25 + olat * 0.5)
        ro = re * sf / Math.pow(ro, sn)
        val rs = LatXLngY()
        if (mode == TO_GRID) {
            rs.lat = lat_X
            rs.lng = lng_Y
            var ra = Math.tan(Math.PI * 0.25 + lat_X * DEGRAD * 0.5)
            ra = re * sf / Math.pow(ra, sn)
            var theta = lng_Y * DEGRAD - olon
            if (theta > Math.PI) theta -= 2.0 * Math.PI
            if (theta < -Math.PI) theta += 2.0 * Math.PI
            theta *= sn
            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5)
            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5)
        } else {
            rs.x = lat_X
            rs.y = lng_Y
            val xn = lat_X - XO
            val yn = ro - lng_Y + YO
            var ra = Math.sqrt(xn * xn + yn * yn)
            if (sn < 0.0) {
                ra = -ra
            }
            var alat = Math.pow(re * sf / ra, 1.0 / sn)
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5
            var theta = 0.0
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0
            } else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5
                    if (xn < 0.0) {
                        theta = -theta
                    }
                } else theta = Math.atan2(xn, yn)
            }
            val alon = theta / sn + olon
            rs.lat = alat * RADDEG
            rs.lng = alon * RADDEG
        }
        return rs
    }

    internal inner class LatXLngY {
        var lat = 0.0
        var lng = 0.0
        var x = 0.0
        var y = 0.0
    }

    companion object {
        var TO_GRID = 0
        var TO_GPS = 1
    }
}