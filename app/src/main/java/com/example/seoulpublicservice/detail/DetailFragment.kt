package com.example.seoulpublicservice.detail

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.location.Location
import android.net.Uri
import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.seoulpublicservice.R
import com.example.seoulpublicservice.databases.ReservationEntity
import com.example.seoulpublicservice.databinding.FragmentDetailBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import kotlin.math.sqrt

private const val DETAIL_PARAM = "detail_param1"
private const val LOCATION_PERMISSION_REQUEST_CODE = 1000

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
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
        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if(!locationSource.isActivated) {
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {   // 여기가 메인
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        viewInit()
        viewModelInit()
        connectToCommentList(requireContext())
        viewModel.closeEvent.observe(viewLifecycleOwner) { close ->
            if(close) dismiss()
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
        binding.ivDetailImg.load(data.IMGURL)   // load with holder?
        binding.let {
            it.tvDetailTypeSmall.text = data.MINCLASSNM
            it.tvDetailName.text = data.SVCNM
            it.tvDetailLocation.text = "${data.AREANM} - ${data.PLACENM}"
            it.tvDetailDistanceFromHere.text = "현위치로부터 ?km"
            it.tvDetailInfo.text = detailInfo(data)
            it.tvDetailDescription.text = data.DTLCONT
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
        naverMap. maxZoom = 15.0
        naverMap. minZoom = 5.0
        naverMap.locationSource = locationSource
        viewModel.serviceData.value?.let {
            bind(it)
        }
        val myLocation = locationSource.lastLocation
        val itemLocation = LatLng(latLng.latitude, latLng.longitude)
        val distance = myLocation?.let { distance(itemLocation, it) } ?: 0.0
        binding.tvDetailDistanceFromHere.text = "현위치로부터 ${String.format("%.1f", distance)}km"
        val marker = Marker()
        marker.position = itemLocation
        marker.map = naverMap
        markerStyle(marker)
        naverMap.cameraPosition = CameraPosition(
            latLng,
            10.0,
            0.0,
            180.0
        )
    }

    private fun markerStyle(marker: Marker) {       // 지도 마커 스타일
        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = Color.RED
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

    // 현재 위치에서부터 아이템까지의 거리
//        val distance = sqrt((locationX - myX) * (locationX - myX) + (locationY - myY) * (locationY - myY))
    private fun distance(loc: LatLng, mine: Location): Double {
        return sqrt((loc.latitude - mine.latitude) * (loc.latitude - mine.latitude) + (loc.longitude - mine.longitude) * (loc.latitude - mine.longitude))
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