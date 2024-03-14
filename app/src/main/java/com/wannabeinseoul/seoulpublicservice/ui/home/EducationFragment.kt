package com.wannabeinseoul.seoulpublicservice.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.ui.main.adapter.ItemAdapter
import com.wannabeinseoul.seoulpublicservice.data.Item
import com.wannabeinseoul.seoulpublicservice.data.ItemRepository
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentEducationBinding
import com.wannabeinseoul.seoulpublicservice.ui.category.CategoryViewModel

class EducationFragment : Fragment() {
    private var _binding: FragmentEducationBinding? = null
    private val binding get() = _binding!!

    private val regionPrefRepository by lazy { (requireActivity().application as SeoulPublicServiceApplication).container.regionPrefRepository }
    private val categoryViewModel: CategoryViewModel by viewModels { CategoryViewModel.factory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEducationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val educationItems = listOf(
            Item(R.drawable.ic_book, "교양/어학"),
            Item(R.drawable.ic_information, "정보통신"),
            Item(R.drawable.ic_history, "역사"),
            Item(R.drawable.ic_science, "자연과학"),
            Item(R.drawable.ic_village, "도시농업"),
            Item(R.drawable.ic_contact, "청년정보"),
            Item(R.drawable.ic_sports, "스포츠"),
            Item(R.drawable.ic_art,  "미술제작"),
            Item(R.drawable.ic_knitting, "공예/취미"),
            Item(R.drawable.ic_certification, "전문/자격증"),
            Item(R.drawable.ic_etc, "기타"),
        )
        ItemRepository.setItems("Education", educationItems)

        val items = ItemRepository.getItems("Education")
        val adapter = ItemAdapter(items, regionPrefRepository, categoryViewModel, viewLifecycleOwner.lifecycleScope)
        binding.rvEducation.adapter = adapter
        binding.rvEducation.layoutManager = GridLayoutManager(requireContext(), 4)
//        homeViewModel.selectedRegion.observe(viewLifecycleOwner) { region ->
//            val selectedRegion = region
//            val adapter = ItemAdapter(items, selectedRegion)
//            binding.rvEducation.adapter = adapter
//            binding.rvEducation.layoutManager = GridLayoutManager(requireContext(), 4)
//        }
    }
}