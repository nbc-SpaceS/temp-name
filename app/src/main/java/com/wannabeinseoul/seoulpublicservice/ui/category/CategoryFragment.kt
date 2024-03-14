package com.wannabeinseoul.seoulpublicservice.ui.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentCategoryBinding
import com.wannabeinseoul.seoulpublicservice.ui.detail.DetailFragment
//ctrl alt o
class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
//    private lateinit var categoryAdapter: CategoryAdapter
    private val viewModel: CategoryViewModel by viewModels{CategoryViewModel.factory}

    private val showDetailFragment = { svcid: String ->
        DetailFragment.newInstance(svcid)
            .show(requireActivity().supportFragmentManager, "Detail")
            }

    private val adapter by lazy {
        CategoryAdapter{}
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

        initView()
        initViewModel()

        categoryClick()
    }

    private fun initView() {
        binding.reCategory.adapter = adapter
        binding.tvCtTitle.text = "${arguments?.getString("region")} - ${arguments?.getString("category")}"
        viewModel.updateList(arguments?.getString("region") ?: "", arguments?.getString("category") ?: "")
        //라이브데이터에 리스트를 넣어놈.

        binding.ivCategoryBack.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun initViewModel() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            Log.d("Observe", "잘 되는지 테스트 ${categories.toString().take(255)}")
            binding.tvCategoryEmptyDescription.isVisible = categories.isEmpty()
            adapter.submitList(categories)
        }
    }

    private fun categoryClick() {  // 인터페이스로 받은 svcid를 상세 페이지로 넘기기
        adapter.categoryItemClick = object : CategoryItemClick {
            override fun onClick(svcID: String) {
                val dialog = DetailFragment.newInstance(svcID)
                dialog.show(requireActivity().supportFragmentManager, "CategoryFrag")
            }
        }
    }
}