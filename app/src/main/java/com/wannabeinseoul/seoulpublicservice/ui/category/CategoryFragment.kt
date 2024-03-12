package com.wannabeinseoul.seoulpublicservice.ui.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentCategoryBinding
import com.wannabeinseoul.seoulpublicservice.detail.DetailFragment
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepositoryImpl

//ctrl alt o
class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding

    //    private lateinit var categoryAdapter: CategoryAdapter
    private val viewModel: CategoryViewModel by viewModels { CategoryViewModel.factory }

    private val showDetailFragment = { svcid: String ->
        DetailFragment.newInstance(svcid)
            .show(requireActivity().supportFragmentManager, "Detail")
    }

    private val adapter by lazy {
        CategoryAdapter {}
    }

    private fun initView() {
        binding.reCategory.adapter = adapter
        viewModel.updateList("")
        //라이브데이터에 리스트를 넣어놈.
    }

    private fun initViewModel() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            Log.d("Observe", "잘 되는지 테스트 ${categories.toString().take(255)}")

            val regionRepository = RegionPrefRepositoryImpl(requireContext())
            // 선택된 지역 가져오기
            val selectedAreas = regionRepository.load()

            // 선택된 지역별로 추천 서비스 리스트 가져오기
            selectedAreas.forEachIndexed { index, area ->
                viewModel.getList(area, index)
                adapter.submitList(categories)
            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivCategoryBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        initView()
        initViewModel()
    }
}