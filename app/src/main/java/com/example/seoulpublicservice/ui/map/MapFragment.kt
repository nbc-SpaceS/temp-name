package com.example.seoulpublicservice.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.seoulpublicservice.R
import com.example.seoulpublicservice.databinding.FragmentMapBinding
import com.example.seoulpublicservice.dialog.filter.FilterFragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private lateinit var naverMap : NaverMap
    private lateinit var locationSource: FusedLocationSource

    private val viewModel: MapViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root = inflater.inflate(R.layout.fragment_map, container, false) as ViewGroup

        mapView = binding.root.findViewById(R.id.mv_naver) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvMapFilterBtn.setOnClickListener {
            val dialog = FilterFragment.newInstance()
            dialog.show(requireActivity().supportFragmentManager, "FilterFragment")
        }
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0

        locationSource = FusedLocationSource(this, 5000)

        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.NoFollow
        naverMap.uiSettings.isLogoClickEnabled = false
        naverMap.uiSettings.isScaleBarEnabled = false
        naverMap.uiSettings.isCompassEnabled = false
        naverMap.uiSettings.setLogoMargin(0, 0, 0, 0)

        val cameraUpdate = CameraUpdate.scrollTo(
            LatLng(
                locationSource.lastLocation?.latitude ?: 37.5839,
                locationSource.lastLocation?.longitude ?: 127.0588
            )
        )
        naverMap.moveCamera(cameraUpdate)


        val marker = Marker()
        marker.position = LatLng(37.5939, 127.0888)
        marker.map = naverMap
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}