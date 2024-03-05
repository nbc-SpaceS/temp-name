package com.wannabeinseoul.seoulpublicservice.detail

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.databases.ReservationEntity
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentDetailBinding
import com.wannabeinseoul.seoulpublicservice.util.loadWithHolder
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

private const val DETAIL_PARAM = "detail_param1"
private const val LOCATION_PERMISSION_REQUEST_CODE = 5000

class DetailFragment : DialogFragment(), OnMapReadyCallback {       // Map 이동 시 ScrollView 잠금 해야됌
    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap

    private var param1: String? = null

    private var _binding: FragmentDetailBinding? = null
    val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels { DetailViewModel.factory }

    private lateinit var commentAdapter: DetailCommentAdapter  // 후기 ListAdapter 선언

    private lateinit var latLng: LatLng
    private lateinit var locationSource: FusedLocationSource

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var myLocation:LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myLocation = LatLng(0.0, 0.0)
        arguments?.let {
            param1 = it.getString(DETAIL_PARAM)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.DetailTransparent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        mapView = binding.root.findViewById(R.id.mv_detail_maps) as MapView
        mapView.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {   // 여기가 메인
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        requestLocationPermission() // 권한 요청
        fetchCallback() // 콜백을 받으려면 필요함
        viewInit()
        viewModelInit()
        connectToCommentList(requireContext())
        viewModel.closeEvent.observe(viewLifecycleOwner) { close ->
            if(close) dismiss()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun getCurrentLocation(callback: (LatLng) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        if (latitude != 0.0 && longitude != 0.0) {
                            val currentLocation = LatLng(latitude, longitude)
                            callback(currentLocation)
                        }
                    }
                }
            viewModel.callbackEvent(true)
        }
    }

    private fun fetchCallback() {
        getCurrentLocation {
            myLocation = it
        }
    }

    private fun viewInit() = binding.let {
        it.btnDetailBack.setOnClickListener { viewModel.close(true) }
        it.btnDetailCall.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${viewModel.serviceData.value?.TELNO}")))
        }
        it.btnDetailReservation.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.serviceData.value?.SVCURL)))
        }
    }

    private fun viewModelInit() = viewModel.let { vm ->
        vm.getData(param1!!)
        vm.serviceData.observe(viewLifecycleOwner) { it ->
            it?.let {
                data -> bind(data)
            }
        }
    }

    private fun bind(data : ReservationEntity) {
        latLng = LatLng(data.Y.toDouble(), data.X.toDouble())   // latitude - 위도(-90 ~ 90) / longitude(-180 ~ 180) - 경도 : 검색할 때 위경도 순으로 검색해야 함
        buttonDesign(data)
        binding.ivDetailImg.loadWithHolder(data.IMGURL)
        binding.let {
            it.tvDetailTypeSmall.text = data.MINCLASSNM
            it.tvDetailName.text = data.SVCNM
            it.tvDetailLocation.text = "${data.AREANM} - ${data.PLACENM}"
            it.tvDetailDistanceFromHere.text = "현위치로부터 ?km"
            it.tvDetailInfo.text = detailInfo(data)
            it.tvDetailDescription.text = data.DTLCONT
        }
    }

    private fun buttonDesign(data: ReservationEntity) {
        var button = binding.btnDetailReservation
        /**
         * 접수중 => 예약하기, 안내중 => 예약안내 // 버튼 활성화(빨간색, 텍스트 흰색)
         * 접수종료, 예약일시중지, 예약마감 // 버튼 비활성화(연한회색, 텍스트 진한 회색)
         */
        when(data.SVCSTATNM) {
            "접수중" -> {
                button.text = "예약하기"
                button.isEnabled = true
            }
            "안내중" -> {
                button.text = "예약안내"
                button.isEnabled = true
            }
            "접수종료" -> {
                button.text = "접수종료"
                button.isEnabled = false
            }
            "예약일시중지" -> {
                button.text = "예약일시중지"
                button.isEnabled = false
            }
            "예약마감" -> {
                button.text = "예약마감"
                button.isEnabled = false
            }
        }
    }

    private fun detailInfo(str: ReservationEntity): SpannableStringBuilder {
        val list = listOf("서비스 대상","서비스 일자","예약 가능 일자","시설 사용 시간","취소 가능 기준")
        var text = "${list[0]} : ${str.USETGTINFO}\n" +
                "${list[1]} : ${simpleDateFormatting(str.SVCOPNBGNDT)} ~ ${simpleDateFormatting(str.SVCOPNENDDT)}\n" +
                "${list[2]} : ${simpleDateFormatting(str.RCPTBGNDT)} ~ ${simpleDateFormatting(str.RCPTENDDT)}\n" +
                "${list[3]} : ${str.V_MIN} ~ ${str.V_MAX}\n" +
                "${list[4]} : ${str.REVSTDDAYNM} ${str.REVSTDDAY}일 전"
        val ssb = SpannableStringBuilder(text)
        for (word in list) {
            var startIndex = text.indexOf(word)
            while (startIndex != -1) {
                val endIndex = startIndex + word.length
                ssb.setSpan(StyleSpan(Typeface.BOLD), startIndex, endIndex, 0)
                startIndex = text.indexOf(word, endIndex)
            }
        }
        return ssb
    }

    override fun onMapReady(nMap: NaverMap) {
        naverMap = nMap
        naverMap.apply{
            maxZoom = 19.0
            minZoom = 11.0
            locationSource = locationSource
            locationTrackingMode = LocationTrackingMode.NoFollow
            cameraPosition = CameraPosition(latLng, 16.0)
            uiSettings.apply {
                isLogoClickEnabled = false
                isScaleBarEnabled = false
                isCompassEnabled = false
                isZoomControlEnabled = true
                isScrollGesturesEnabled = false
                setLogoMargin(0,0,0,0)
            }
            viewModel.serviceData.value?.let { bind(it) }
            val itemLocation = LatLng(latLng.latitude, latLng.longitude)
            viewModel.callbackEvent.value.let {
                val distance = distance(itemLocation, myLocation)
                binding.tvDetailDistanceFromHere.text =
                    if(distance/1000 < 1) "현위치로부터 ${String.format("%.0f", distance)}m"
                    else "현위치로부터 ${String.format("%.1f", distance/1000)}km"
            }
            val marker = Marker()
            marker.position = itemLocation
            marker.map = naverMap
            markerStyle(marker)
        }
    }

    private fun markerStyle(marker: Marker) {
        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = requireContext().getColor(R.color.point_color)
        marker.width = 80
        marker.height = 100
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.close(false)
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
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun simpleDateFormatting(date: String): String {    // 날짜 변환 해야됌
        return date
    }

    private fun connectToCommentList(context: Context) {        // 후기 어댑터 연결
        val sample = DetailCommentSample().dataList
        commentAdapter = DetailCommentAdapter()
        binding.rvDetailReview.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        commentAdapter.submitList(sample)
    }

    // 두 지점 간의 직선 거리를 계산하는 함수
    private fun distance(point1: LatLng, point2: LatLng): Double {
        val R = 6371 // 지구의 반지름 (단위: km)

        val latDistance = Math.toRadians(point2.latitude - point1.latitude)
        val lonDistance = Math.toRadians(point2.longitude - point1.longitude)
        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                (cos(Math.toRadians(point1.latitude)) * cos(Math.toRadians(point2.latitude)) *
                        sin(lonDistance / 2) * sin(lonDistance / 2))
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c * 1000 // 단위를 미터로 변환
    }
    companion object {
        @JvmStatic
        fun newInstance(serviceID: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(DETAIL_PARAM, serviceID)
                }
            }
    }
}