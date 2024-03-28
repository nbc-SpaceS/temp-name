package com.wannabeinseoul.seoulpublicservice.ui.main

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.naver.maps.map.util.FusedLocationSource
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databinding.ActivityMainBinding
import com.wannabeinseoul.seoulpublicservice.util.DLog

private const val JJTAG = "jj-메인액티비티"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val app by lazy { application as SeoulPublicServiceApplication }

    private val viewPagerAdapter by lazy {
        ViewPagerAdapter(this@MainActivity)
    }

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app.fusedLocationSource = FusedLocationSource(this, 5000)
        requestPermissions(permissions, 5000)  // 권한이 있든 없든 그냥 불러

        binding.vpMain.adapter = viewPagerAdapter
        binding.vpMain.isUserInputEnabled = false
        binding.vpMain.offscreenPageLimit = 4
        binding.vpMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.navView.menu.getItem(position).isChecked = true
            }
        })

        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    binding.vpMain.setCurrentItem(0, false)
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_map -> {
                    binding.vpMain.setCurrentItem(1, false)
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_recommendation -> {
                    binding.vpMain.setCurrentItem(2, false)
                    return@setOnItemSelectedListener true
                }

                else -> {
                    binding.vpMain.setCurrentItem(3, false)
                    return@setOnItemSelectedListener true
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        DLog.d(
            JJTAG, "onRequestPermissionsResult requestCode: $requestCode" +
                    ", permissions: ${permissions.contentToString()}" +
                    ", grantResults: ${grantResults.contentToString()}"
        )
        val fusedLocationSource = app.fusedLocationSource
        if (fusedLocationSource != null &&
            fusedLocationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)
        ) {
            DLog.d(
                JJTAG, "onRequestPermissionsResult " +
                        "fusedLocationSource.isActivated: ${fusedLocationSource.isActivated}"
            )
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
