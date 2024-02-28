package com.example.seoulpublicservice.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.seoulpublicservice.R
import com.example.seoulpublicservice.SeoulPublicServiceApplication
import com.example.seoulpublicservice.databinding.FragmentCategoryBinding
import com.example.seoulpublicservice.pref.RegionPrefRepository

class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val regionPrefRepository: RegionPrefRepository by lazy {
        (requireActivity().application as SeoulPublicServiceApplication).container.regionPrefRepository
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedRegions = regionPrefRepository.load()
    }

}