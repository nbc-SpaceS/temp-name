package com.example.seoulpublicservice.ui.map

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.seoulpublicservice.R
import com.example.seoulpublicservice.databinding.FragmentMapBinding
import com.example.seoulpublicservice.dialog.filter.FilterFragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private val activeMarkers: MutableList<Marker> = mutableListOf()

    private val adapter: MapDetailInfoAdapter by lazy {
        MapDetailInfoAdapter(
            moveReservationPage = { url ->
                viewModel.moveReservationPage(url)
            },
            shareUrl = { url ->
                viewModel.shareReservationPage(url)
            },
            moveDetailPage = { id ->
                viewModel.moveDetailPage(id)
            }
        )
    }

    private val viewModel: MapViewModel by viewModels { MapViewModel.factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        mapView = binding.root.findViewById(R.id.mv_naver) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        locationSource = FusedLocationSource(this, 5000)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        viewModel.initMap()
        viewModel.loadSavedOptions()

        binding.vpMapDetailInfo.adapter = adapter
        binding.vpMapDetailInfo.registerOnPageChangeCallback(object : OnPageChangeCallback() {})
        binding.vpMapDetailInfo.offscreenPageLimit = 1

        binding.tvMapFilterBtn.setOnClickListener {
            val dialog = FilterFragment.newInstance(
                onClickButton = {
                    viewModel.loadSavedOptions()
                }
            )
            dialog.show(requireActivity().supportFragmentManager, "FilterFragment")
        }
    }

    private fun initViewModel() = with(viewModel) {

        hasFilter.observe(viewLifecycleOwner) {
            if (it) {
                binding.tvMapFilterBtn.setTextColor(requireContext().getColor(R.color.point_color))
            } else {
                binding.tvMapFilterBtn.setTextColor(requireContext().getColor(R.color.black))
            }
        }

        visibleInfoWindow.observe(viewLifecycleOwner) {
            binding.vpMapDetailInfo.isVisible = it
        }

        updateData.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list.toList())
        }

        moveToUrl.observe(viewLifecycleOwner) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(it)
                )
            )
        }

        shareUrl.observe(viewLifecycleOwner) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/html"
            val url = it
            intent.putExtra(Intent.EXTRA_TEXT, url)
            val text = "공유하기"
            startActivity(Intent.createChooser(intent, text))
        }

        detailInfoId.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "${it}의 상세페이지로 이동", Toast.LENGTH_SHORT).show()
        }

        canStart.observe(viewLifecycleOwner) { start ->
            if (start) {
                activeMarkers.forEach {
                    it.map = null
                }
                activeMarkers.clear()

                filteringData.value?.forEach {
                    val marker = Marker()
                    marker.position = LatLng(it.key.first.toDouble(), it.key.second.toDouble())
                    marker.map = naverMap
                    marker.icon = MarkerIcons.BLACK
                    marker.iconTintColor = requireContext().getColor(R.color.point_color)
                    marker.tag = it.key
                    marker.onClickListener = Overlay.OnClickListener { _ ->
                        viewModel.changeVisible(true)
                        viewModel.updateInfo(it.value)
                        true
                    }
                    activeMarkers.add(marker)
                }
            }
        }
    }

    override fun onMapReady(map: NaverMap) {
        var isFirst = false

        naverMap = map
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0

        viewModel.checkReadyMap()

        naverMap.setOnMapClickListener { pointF, latLng ->
            viewModel.changeVisible(false)
        }

        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.NoFollow
        naverMap.uiSettings.isLogoClickEnabled = false
        naverMap.uiSettings.isScaleBarEnabled = false
        naverMap.uiSettings.isCompassEnabled = false
        naverMap.uiSettings.isZoomControlEnabled = false
        naverMap.uiSettings.setLogoMargin(0, 0, 0, 0)

        naverMap.addOnLocationChangeListener { location ->
            if (!isFirst) {
                val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                    LatLng(
                        location.latitude,
                        location.longitude
                    ),
                    15.0
                ).animate(CameraAnimation.Easing, 600)

                naverMap.moveCamera(cameraUpdate)
                isFirst = true
            }
        }
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
        viewModel.clearData()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearData()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}