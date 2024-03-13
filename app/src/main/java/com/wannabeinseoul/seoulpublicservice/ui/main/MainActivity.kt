package com.wannabeinseoul.seoulpublicservice.ui.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!hasPermission()) {
            requestPermissions(permissions, 5000)
        }

        // 앱의 lastLocation 갱신해두기
        /*
        앱 첫 실행 시 requestPermissions이 비동기인지 권한 받기 전에 아래가 실행되면서 권한 체크에서 터짐.
        권한 받고 나면 실행시키고 싶은데 콜백도 없는 것 같고.. 모르겠다...
         */
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withTimeout(5_000L) {
                    // 권한 체크
                    if (ActivityCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        error("메인 액티비티 - gps 권한이 없어서 터짐")
                    }

                    val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val app = (application as SeoulPublicServiceApplication)

                    app.lastLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                    lm.getCurrentLocation(
                        LocationManager.GPS_PROVIDER,
                        null,
                        application.mainExecutor
                    ) {
                        app.lastLocation = it
                    }

                    Log.d("jj-메인액티비티", "위치 갱신: ${app.lastLocation}")
                }
            } catch (e: Throwable) {
                Log.e("jj-메인액티비티", "위치 갱신하다 터짐: $e")
            }
        }

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
}
