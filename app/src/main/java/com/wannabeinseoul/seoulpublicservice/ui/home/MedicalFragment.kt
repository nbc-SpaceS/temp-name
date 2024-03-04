package com.wannabeinseoul.seoulpublicservice.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.adapter.ItemAdapter
import com.wannabeinseoul.seoulpublicservice.data.Item
import com.wannabeinseoul.seoulpublicservice.data.ItemRepository
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentMedicalBinding

class MedicalFragment : Fragment() {
    private var _binding: FragmentMedicalBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMedicalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val medicalItems = listOf(
            Item(R.drawable.ic_hospital, "병원"),
            Item(R.drawable.ic_bus, "장애인버스"),
            Item(R.drawable.ic_childhospital, "어린이병원"),
        )
        ItemRepository.setItems("Medical", medicalItems)

        val items = ItemRepository.getItems("Medical")

        val adapter = ItemAdapter(items)
        binding.rvMedical.adapter = adapter
        binding.rvMedical.layoutManager = GridLayoutManager(requireContext(), 4)
    }
}