package com.wannabeinseoul.seoulpublicservice.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.adapter.ItemAdapter
import com.wannabeinseoul.seoulpublicservice.data.Item
import com.wannabeinseoul.seoulpublicservice.data.ItemRepository
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentFacilityBinding

class FacilityFragment : Fragment() {
    private var _binding: FragmentFacilityBinding? = null
    private val binding get() = _binding!!

    private val regionPrefRepository by lazy { (requireActivity().application as SeoulPublicServiceApplication).container.regionPrefRepository }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFacilityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val facilityItems = listOf(
            Item(R.drawable.ic_soccer, "축구장"),
            Item(R.drawable.ic_tennis, "테니스장"),
            Item(R.drawable.ic_pingpong, "탁구장"),
            Item(R.drawable.ic_golf, "골프장"),
            Item(R.drawable.ic_baseball, "야구장"),
            Item(R.drawable.ic_volleyball, "배구장"),
            Item(R.drawable.ic_footvolleyball, "족구장"),
            Item(R.drawable.ic_futsal, "풋살장"),
            Item(R.drawable.ic_badminton, "배드민턴장"),
            Item(R.drawable.ic_stadium, "다목적 경기장"),
            Item(R.drawable.ic_gym, "체육관"),
            Item(R.drawable.ic_basketball, "농구장"),
        )
        ItemRepository.setItems("Facility", facilityItems)

        val items = ItemRepository.getItems("Facility")
        val selectedRegion = regionPrefRepository.load().firstOrNull() ?: ""
        val adapter = ItemAdapter(items, selectedRegion)
        binding.rvFacility.adapter = adapter
        binding.rvFacility.layoutManager = GridLayoutManager(requireContext(), 4)
    }

}