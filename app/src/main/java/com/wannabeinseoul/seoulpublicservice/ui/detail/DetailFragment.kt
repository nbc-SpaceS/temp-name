package com.wannabeinseoul.seoulpublicservice.ui.detail

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import com.wannabeinseoul.seoulpublicservice.MainViewModel
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.databases.ReservationEntity
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentDetailBinding
import com.wannabeinseoul.seoulpublicservice.dialog.review.ReviewFragment
import com.wannabeinseoul.seoulpublicservice.util.loadWithHolder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

private const val DETAIL_PARAM = "detail_param1"

class DetailFragment : DialogFragment(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap

    private var _binding: FragmentDetailBinding? = null
    val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels { DetailViewModel.factory }
    private val mainViewModel: MainViewModel by activityViewModels()

    private var param1: String? = null
    private var textOpen = false    // 텍스트 뷰가 펼쳐져 있는지(false = 접힌 상태, true = 펼친 상태)

    private lateinit var commentAdapter: DetailCommentAdapter  // 후기 ListAdapter 선언

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var myLocation: LatLng  // 내 위치
    private lateinit var itemLocation: LatLng // 아이템 위치

    private var myLocCall = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(DETAIL_PARAM)
        }
        viewModel.getData(param1!!)
        viewModel.savedID(param1!!)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) = Dialog(requireContext(), R.style.DetailTransparent)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        mapView = binding.root.findViewById(R.id.mv_detail_maps) as MapView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
//        binding.mvDetailMaps.visibility = View.VISIBLE
//        binding.ivDetailMapsSnapshot.visibility = View.INVISIBLE
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        favorite(viewModel.savedID.value!!)
        fetchCallback()
        connectToCommentList(requireContext())
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
                        if (latitude != 0.0 && longitude != 0.0 && !latitude.toString().contains("37.42") && !longitude.toString().contains("-122.08")) {
                            val currentLocation = LatLng(latitude, longitude)
                            callback(currentLocation)
                        }
                    }
                }
        } else {
            val currentLocation = LatLng(100.0, 100.0)
            callback(currentLocation)
        }
    }

    private fun fetchCallback() {
        getCurrentLocation {
            myLocation = it
            viewModel.myLocationCallbackEvent(true)

            viewModelInit()
            viewInit()
            mapView.getMapAsync(this)
        }
    }

    private fun viewInit() = binding.let {
        it.btnDetailBack.setOnClickListener { viewModel.close(true) }
        it.tvDetailShowMore.setOnClickListener {
            viewModel.textOpened(!textOpen)
            showMore(textOpen)
        }
        it.ivDetailFavorite.setOnClickListener {
            viewModel.changeFavorite(param1!!)
        }
        it.btnDetailCall.setOnClickListener { startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${viewModel.serviceData.value?.TELNO}"))) }
        it.btnDetailReservation.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.serviceData.value?.SVCURL))) }
        it.ivDetailShare.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/html"
            val url = viewModel.serviceData.value!!.SVCURL
            i.putExtra(Intent.EXTRA_TEXT, url)
            startActivity(Intent.createChooser(i, "링크 공유"))
        }
        it.tvDetailReviewMoveBtn.setOnClickListener {
            mainViewModel.setServiceId(param1!!)
            val bottomSheet = ReviewFragment()
            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        }
    }

    private fun viewModelInit() = viewModel.let { vm ->
        vm.myLocationCallback.observe(viewLifecycleOwner) {
            if(it) myLocCall = true
        }
        vm.serviceData.observe(viewLifecycleOwner) { data ->
            checkLatLng(data)
            bind(data)
            if (itemLocation.isValid && myLocation.isValid) distanceCheck()
        }
        vm.setReviews(param1!!)
        vm.textState.observe(viewLifecycleOwner) {
            textOpen = it
            showMore(it)
        }
        vm.closeEvent.observe(viewLifecycleOwner) { close ->
            if(close) dismiss()
        }
        vm.reviewUiState.observe(viewLifecycleOwner) {
            commentAdapter.submitList(it)
            binding.tvDetailEmptyDescription.isVisible = it.isEmpty()
            mainViewModel.setCurrentReviewList(it)
        }
        vm.favoriteChanged.observe(viewLifecycleOwner) {
            favorite(it)
        }
        mainViewModel.refreshReviewListState.observe(viewLifecycleOwner) {
            vm.setReviews(param1!!)
        }
//        vm.mapSettingFinished.observe(viewLifecycleOwner) {
//            if(it) {
//                snapshotCallback()
//            }
//        }
    }

    private fun bind(data : ReservationEntity) {
        buttonDesign(data)
        binding.ivDetailImg.loadWithHolder(data.IMGURL)
        binding.let {
            it.tvDetailTypeSmall.text = data.MINCLASSNM
            it.tvDetailName.text = data.SVCNM
            it.tvDetailLocation.text = "${data.AREANM} - ${Html.fromHtml(data.PLACENM, Html.FROM_HTML_MODE_LEGACY)}"
            it.tvDetailDistanceFromHere.text = "현위치로부터 ?km"
            it.tvDetailUsetgtinfo.text = data.USETGTINFO.trim()
            it.tvDetailSvcopndt.text = "${dateFormat(data.SVCOPNBGNDT)} ~ ${dateFormat(data.SVCOPNENDDT)}"
            it.tvDetailRcptdt.text = "${dateFormat(data.RCPTBGNDT)} ~ ${dateFormat(data.RCPTENDDT)}"
            it.tvDetailV.text = "${data.V_MIN} ~ ${data.V_MAX}"
            it.tvDetailRevstdday.text = "${data.REVSTDDAYNM} ${data.REVSTDDAY}일 전"
            it.tvDetailDescription.text = Html.fromHtml(data.DTLCONT, Html.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun checkLatLng(data: ReservationEntity): LatLng {  // 마커의 위치 처리
        val x = data.X.toDoubleOrNull()
        val y = data.Y.toDoubleOrNull()
        itemLocation = if(x != null && y != null) {
            LatLng(y.toDouble(), x.toDouble())   // latitude - 위도(-90 ~ 90) / longitude(-180 ~ 180) - 경도 : 검색할 때 위경도 순으로 검색해야 함
        } else {
            LatLng(100.0, 100.0)
        }
        return itemLocation
    }

    private fun distanceCheck() {
        val distance = distance(itemLocation, myLocation)
        binding.tvDetailDistanceFromHere.text =
            when {
                distance/1000 < 1 && distance <= 150000 -> "현위치로부터 ${String.format("%.0f", distance)}m"
                distance/1000 >= 1 && distance <= 150000 -> "현위치로부터 ${String.format("%.1f", distance/1000)}km"
                else -> "현위치로부터 ?km"
            }
    }

    override fun onMapReady(nMap: NaverMap) {
        naverMap = nMap
        naverMap.apply {
            maxZoom = 19.0
            minZoom = 11.0
            locationSource = locationSource
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // GPS 권한 없으면 이거 하면 멈춰버려서 권한 체크로 감싸줌
                locationTrackingMode = LocationTrackingMode.NoFollow
            }
            cameraPosition = CameraPosition(itemLocation, 14.0)
            uiSettings.apply {
                isLogoClickEnabled = false
                isScaleBarEnabled = false
                isCompassEnabled = false
                isZoomControlEnabled = false
                isScrollGesturesEnabled = false
                isScaleBarEnabled = false
                isRotateGesturesEnabled = false
                isZoomGesturesEnabled = false
                isIndoorLevelPickerEnabled = false
                isLocationButtonEnabled = false
                isTiltGesturesEnabled = false
                setLogoMargin(0, 0, 0, 0)
//                Log.i("This is DetailFragment","onMapReady : setLogoMargin")
            }
            markerStyle()
        }
//        Log.i("This is DetailFragment","mapFinish: ")
//        viewModel.mapFinish(true)
    }

    private fun markerStyle() {
//        Log.i("This is DetailFragment","markerStyle : ")
        val marker = Marker()
        marker.position = itemLocation
        marker.map = naverMap
        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = requireContext().getColor(R.color.point_color)
        marker.width = 80
        marker.height = 100
    }
//
//    private fun snapshotCallback() {
//        Log.i("This is DetailFragment","Snapshot Ready : ")
//        naverMap.takeSnapshot {
//            Log.i("This is DetailFragment","take Snapshot : $it")
//            binding.ivDetailMapsSnapshot.loadWithHolder(it)
//            binding.ivDetailMapsSnapshot.visibility = View.VISIBLE
//            binding.mvDetailMaps.visibility = View.GONE
//            mapView.onDestroy()
//        }
//    }

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
//        Log.i("This is DetailFragment","onResume : ")
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
//        viewModel.mapFinish(false)
        _binding = null
        dialog?.dismiss()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun dateFormat(date: String): String {
        val datePattern = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")
        val dateTime = LocalDateTime.parse(date, formatter)
        return datePattern.format(dateTime)
    }

    private fun showMore(state : Boolean) {
        val text = binding.tvDetailDescription
        val more = binding.tvDetailShowMore
        val layoutParams = text.layoutParams
        more.let {
            when(state) {
                true -> {   // 펼쳐진 상태일 때
                    text.maxLines = Int.MAX_VALUE
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    more.text = "접기..."
                }
                false -> {  // 접혀있는 상태일 때
                    text.maxLines = 6
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    more.text = "더보기..."
                }
            }
            text.layoutParams = layoutParams
        }
    }

    private fun favorite(state: Boolean) {
        when(state) {
            true -> binding.ivDetailFavorite.setImageResource(R.drawable.ic_star_color)
            false -> binding.ivDetailFavorite.setImageResource(R.drawable.ic_star_empty)
        }
    }

    // 후기 어댑터 연결(임시)
    private fun connectToCommentList(context: Context) {
        commentAdapter = DetailCommentAdapter()
        binding.rvDetailReview.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
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


    private fun buttonDesign(data: ReservationEntity) {
        val button = binding.btnDetailReservation
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
        val telBtn = binding.btnDetailCall
        when {
            data.TELNO.isBlank() -> telBtn.isEnabled = false
            data.TELNO.isNotBlank() -> telBtn.isEnabled = true
        }
        val payment = binding.tvDetailPrice
        payment.text = data.PAYATNM
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