package com.wannabeinseoul.seoulpublicservice.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.ui.main.adapter.ItemAdapter
import com.wannabeinseoul.seoulpublicservice.data.Item
import com.wannabeinseoul.seoulpublicservice.data.ItemRepository
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentMedicalBinding
import com.wannabeinseoul.seoulpublicservice.ui.category.CategoryViewModel
import com.wannabeinseoul.seoulpublicservice.ui.main.MainViewModel

class MedicalFragment : Fragment() {
    private var _binding: FragmentMedicalBinding? = null
    private val binding get() = _binding!!

    private val regionPrefRepository by lazy { (requireActivity().application as SeoulPublicServiceApplication).container.regionPrefRepository }
    private val categoryViewModel: CategoryViewModel by viewModels { CategoryViewModel.factory }

    private val dbMemoryRepository by lazy { (requireActivity().application as SeoulPublicServiceApplication).container.dbMemoryRepository }
    private val mainViewModel: MainViewModel by activityViewModels()
    private val adapter by lazy {
        ItemAdapter(regionPrefRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMedicalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var medicalItems = listOf(
            Item(R.drawable.ic_hospital, "병원"),
            Item(R.drawable.ic_bus, "장애인버스"),
            Item(R.drawable.ic_childhospital, "어린이병원"),
        )
        ItemRepository.setItems("Medical", medicalItems)

        binding.rvMedical.adapter = adapter
        binding.rvMedical.layoutManager = GridLayoutManager(requireContext(), 4)
        adapter.submitList(medicalItems)

//        val items = ItemRepository.getItems("Medical")
//        val adapter = ItemAdapter(regionPrefRepository)
//        binding.rvMedical.adapter = adapter
//        binding.rvMedical.layoutManager = GridLayoutManager(requireContext(), 4)

//        homeViewModel.selectedRegion.observe(viewLifecycleOwner) { region ->
//            val selectedRegion = region
//            val adapter = ItemAdapter(items, selectedRegion)
//            binding.rvMedical.adapter = adapter
//            binding.rvMedical.layoutManager = GridLayoutManager(requireContext(), 4)
//        }

        mainViewModel.selectRegion.observe(viewLifecycleOwner) {
            if (it != "지역선택") {
                medicalItems = medicalItems.map { item ->
                    val size = dbMemoryRepository.getFiltered(
                        areanm = listOf(it.toString()),
                        minclassnm = listOf(item.name)
                    ).size
                    item.copy(count = size)
                }
                adapter.submitList(medicalItems)
            }
        }
    }
}