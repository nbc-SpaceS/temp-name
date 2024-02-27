package com.example.seoulpublicservice.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.seoulpublicservice.R
import com.example.seoulpublicservice.databases.ReservationEntity

private const val DETAIL_PARAM = "detail_param1"

class DetailFragment : DialogFragment() {
    private var param1: ReservationEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getParcelable(DETAIL_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: ReservationEntity) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(DETAIL_PARAM, param1)
                }
            }
    }
}