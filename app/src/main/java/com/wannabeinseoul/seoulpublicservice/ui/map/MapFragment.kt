package com.wannabeinseoul.seoulpublicservice.ui.map

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.dialog.filter.FilterFragment
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
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentMapBinding
import com.wannabeinseoul.seoulpublicservice.detail.DetailFragment

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private val app by lazy {
        requireActivity().application as SeoulPublicServiceApplication
    }
    private val container by lazy {
        app.container
    }

    private val activeMarkers: MutableList<Marker> = mutableListOf()

    private val rvAdapter: MapOptionAdapter by lazy {
        MapOptionAdapter()
    }

    private val adapter: MapDetailInfoAdapter by lazy {
        MapDetailInfoAdapter(
            saveService = { id ->
                viewModel.saveService(id)
            },
            moveReservationPage = { url ->
                viewModel.moveReservationPage(url)
            },
            shareUrl = { url ->
                viewModel.shareReservationPage(url)
            },
            moveDetailPage = { id ->
                viewModel.moveDetailPage(id)
            },
            savedPrefRepository = container.savedPrefRepository
        )
    }

    private val viewModel: MapViewModel by viewModels { MapViewModel.factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(
            inflater,
            container,
            false
        )

        mapView = binding.root.findViewById(R.id.mv_naver) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        locationSource = FusedLocationSource(this, 5000)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() {
        binding.vpMapDetailInfo.adapter = adapter
        binding.vpMapDetailInfo.registerOnPageChangeCallback(object : OnPageChangeCallback() {})
        binding.vpMapDetailInfo.offscreenPageLimit = 1

        binding.rvMapSelectedOption.adapter = rvAdapter
        rvAdapter.submitList(app.container.filterPrefRepository.load().flatten())

        binding.tvMapFilterBtn.setOnClickListener {
            val dialog = FilterFragment.newInstance(
                onClickButton = {
                    viewModel.loadSavedOptions()
                    rvAdapter.submitList(app.container.filterPrefRepository.load().flatten())
                }
            )
            dialog.show(requireActivity().supportFragmentManager, "FilterFragment")
        }

        binding.fabMapCurrentLocation.setOnClickListener {
            moveCamera(locationSource.lastLocation?.latitude, locationSource.lastLocation?.longitude)
        }
    }

    private fun initViewModel() = with(viewModel) {
        initMap()
        loadSavedOptions()

        hasFilter.observe(viewLifecycleOwner) {
            if (it) {
                binding.tvMapFilterBtn.setTextColor(requireContext().getColor(R.color.point_color))
                binding.clMapFilterCount.isVisible = true
                binding.tvMapFilterCount.text = filterCount.toString()
            } else {
                binding.tvMapFilterBtn.setTextColor(requireContext().getColor(R.color.black))
                binding.clMapFilterCount.isVisible = false
            }
        }

        visibleInfoWindow.observe(viewLifecycleOwner) {
            binding.vpMapDetailInfo.isVisible = it
            binding.clMapInfoCount.isVisible = it
            binding.fabMapCurrentLocation.isVisible = !it
        }

        updateData.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list.toList())
            binding.tvMapInfoCount.text = list.size.toString()
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
            val dialog = DetailFragment.newInstance(it)
            dialog.show(requireActivity().supportFragmentManager, "Detail")
        }

        canStart.observe(viewLifecycleOwner) { start ->
            if (start) {
                activeMarkers.forEach {
                    it.map = null
                }
                activeMarkers.clear()

                filteringData.value?.forEach {
                    val marker = Marker()
                    activeMarkers.add(marker)
                    marker.position = LatLng(it.key.first.toDouble(), it.key.second.toDouble())
                    marker.map = naverMap
                    marker.icon = MarkerIcons.BLACK
                    marker.iconTintColor = requireContext().getColor(R.color.point_color)
                    marker.tag = it.key
                    marker.onClickListener = Overlay.OnClickListener { _ ->
                        viewModel.changeVisible(true)
                        activeMarkers.forEach { marker ->
                            marker.iconTintColor = requireContext().getColor(R.color.point_color)
                            marker.zIndex = 0
                        }
                        marker.iconTintColor =
                            requireContext().getColor(R.color.clicked_marker_solid)
                        marker.zIndex = 10
                        viewModel.updateInfo(it.value)
                        moveCamera(it.key.first.toDouble(), it.key.second.toDouble())
                        true
                    }
                }
            }
        }
    }

    override fun onMapReady(map: NaverMap) {

        naverMap = map
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0

        viewModel.checkReadyMap()

        naverMap.setOnMapClickListener { pointF, latLng ->
            viewModel.changeVisible(false)
            activeMarkers.forEach { marker ->
                marker.iconTintColor = requireContext().getColor(R.color.point_color)
                marker.zIndex = 0
            }
        }

        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.NoFollow
        naverMap.uiSettings.isLogoClickEnabled = false
        naverMap.uiSettings.isScaleBarEnabled = false
        naverMap.uiSettings.isCompassEnabled = false
        naverMap.uiSettings.isZoomControlEnabled = false
        naverMap.uiSettings.setLogoMargin(0, 0, 0, 0)

        if (app.lastLocation == null) {
            moveCamera(locationSource.lastLocation?.latitude, locationSource.lastLocation?.longitude)
        }

        naverMap.addOnLocationChangeListener { location ->
            app.lastLocation = location
        }
    }

    private fun moveCamera(y: Double?, x: Double?) {
        val cameraUpdate = CameraUpdate.scrollAndZoomTo(
            LatLng(
                y ?: app.lastLocation?.latitude ?: 37.5666,
                x ?: app.lastLocation?.longitude ?: 126.9782
            ),
            15.0
        ).animate(CameraAnimation.Easing, 600)

        naverMap.moveCamera(cameraUpdate)
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