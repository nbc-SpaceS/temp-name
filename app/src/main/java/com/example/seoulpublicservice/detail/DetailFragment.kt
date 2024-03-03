package com.example.seoulpublicservice.detail

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.seoulpublicservice.R
import com.example.seoulpublicservice.databases.ReservationEntity
import com.example.seoulpublicservice.databinding.FragmentDetailBinding
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback

private const val DETAIL_PARAM = "detail_param1"

class DetailFragment : DialogFragment(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap
    private var param1: String? = null

    private var _binding: FragmentDetailBinding? = null
    val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels { DetailViewModel.factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        mapView.onCreate(savedInstanceState)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewInit()
        viewModelInit()

        viewModel.closeEvent.observe(viewLifecycleOwner) { close ->
            if(close) dismiss()
        }
    }

    private fun viewInit() = binding.let {
        it.btnDetailBack.setOnClickListener { viewModel.close(true) }
        it.btnDetailCall.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.serviceData.value?.TELNO)))
        }
        it.btnDetailReservation.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.serviceData.value?.SVCURL)))
        }
    }

    private fun viewModelInit() = viewModel.let {
        viewModel.getData(param1!!)
        viewModel.serviceData.observe(viewLifecycleOwner) {
            bind(it, requireContext())
        }
    }

    private fun bind(data : ReservationEntity, context: Context) {
        Glide.with(context)
            .load(data.IMGURL)
            .into(binding.ivDetailImg)
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
        var text = "${list[0]} : ${str.USETGTINFO.take(20)}\n" +
                "${list[1]} : ${str.SVCOPNBGNDT} ~ ${str.SVCOPNENDDT}\n" +
                "${list[2]} : ${str.RCPTBGNDT} ~ ${str.RCPTENDDT}\n" +
                "${list[3]} : ${str.V_MIN} ~ ${str.V_MAX}" +
                "${list[4]} : ${str.REVSTDDAYNM} ${str.REVSTDDAY}일 전"
        val ssb = SpannableStringBuilder(text)
        for (word in list) {
            val startIndex = text.indexOf(word)
            if (startIndex != -1) {
                val endIndex = startIndex + word.length
                val spannableString = SpannableString(text.subSequence(startIndex, endIndex))
                spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, word.length, 0)
                ssb.replace(startIndex, endIndex, spannableString)
            }
        }
        return ssb
    }

    override fun onMapReady(nMap: NaverMap) {
        naverMap = nMap
        naverMap. maxZoom = 16.0
        naverMap. minZoom = 8.0
    }

    // 현재 위치에서부터 아이템까지의 거리
    // val distance = sqrt((itemX - currentX) * (itemX - currentX) + (itemY - currentY) * (itemY - currentY))

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