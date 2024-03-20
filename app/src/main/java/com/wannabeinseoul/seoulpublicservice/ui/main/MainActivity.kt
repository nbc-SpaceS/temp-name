package com.wannabeinseoul.seoulpublicservice.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.naver.maps.map.util.FusedLocationSource
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databinding.ActivityMainBinding

private const val JJTAG = "jj-메인액티비티"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val app by lazy { application as SeoulPublicServiceApplication }

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app.fusedLocationSource = FusedLocationSource(this, 5000)

//        if (!hasPermission()) {
        requestPermissions(permissions, 5000)  // 권한이 있든 없든 그냥 불러
//        }

//        // 앱의 lastLocation 갱신해두기
//        /*
//        앱 첫 실행 시 requestPermissions이 비동기인지 권한 받기 전에 아래가 실행되면서 권한 체크에서 터짐.
//        권한 받고 나면 실행시키고 싶은데 콜백도 없는 것 같고.. 모르겠다...
//         */
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                withTimeout(5_000L) {
//                    // 권한 체크
//                    if (ActivityCompat.checkSelfPermission(
//                            this@MainActivity,
//                            Manifest.permission.ACCESS_FINE_LOCATION
//                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                            this@MainActivity,
//                            Manifest.permission.ACCESS_COARSE_LOCATION
//                        ) != PackageManager.PERMISSION_GRANTED
//                    ) {
//                        error("메인 액티비티 - gps 권한이 없어서 터짐")
//                    }
//
//                    val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//                    app.lastLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                        lm.getCurrentLocation(
//                            LocationManager.GPS_PROVIDER,
//                            null,
//                            application.mainExecutor
//                        ) {
//                            app.lastLocation = it
//                        }
//                    }
//
//                    Log.d(JJTAG, "위치 갱신: ${app.lastLocation}")
//                }
//            } catch (e: Throwable) {
//                Log.e(JJTAG, "위치 갱신하다 터짐: $e")
//            }
//        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
    }

    private fun hasPermission(): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d(
            JJTAG, "onRequestPermissionsResult requestCode: $requestCode" +
                    ", permissions: ${permissions.contentToString()}" +
                    ", grantResults: ${grantResults.contentToString()}"
        )
        val fusedLocationSource = app.fusedLocationSource
        if (fusedLocationSource != null &&
            fusedLocationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)
        ) {
            Log.d(
                JJTAG, "onRequestPermissionsResult " +
                        "fusedLocationSource.isActivated: ${fusedLocationSource.isActivated}"
            )
            if (!fusedLocationSource.isActivated) { // 권한 거부됨
//                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
