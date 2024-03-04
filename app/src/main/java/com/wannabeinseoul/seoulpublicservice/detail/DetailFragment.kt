package com.wannabeinseoul.seoulpublicservice.detail

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.wannabeinseoul.seoulpublicservice.databases.ReservationEntity
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentDetailBinding

private const val DETAIL_PARAM = "detail_param1"

class DetailFragment : DialogFragment() {
    private var param1: String? = null

    private var _binding: FragmentDetailBinding? = null
    val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(DETAIL_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
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
    }

    private fun viewModelInit() {
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
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.close(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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