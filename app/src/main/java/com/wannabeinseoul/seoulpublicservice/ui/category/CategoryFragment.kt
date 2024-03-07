package com.wannabeinseoul.seoulpublicservice.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentCategoryBinding
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentRecommendationBinding
import com.wannabeinseoul.seoulpublicservice.detail.DetailFragment
import com.wannabeinseoul.seoulpublicservice.pref.CategoryPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.CategoryPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.ui.recommendation.RecommendationViewModel

class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var viewModel: CategoryViewModel
    private lateinit var categoryPrefRepository: CategoryPrefRepository
    private lateinit var adapter: CategoryAdapter

    private val app by lazy { requireActivity().application as SeoulPublicServiceApplication }
    private val dbMemoryRepository by lazy { app.container.dbMemoryRepository }

    private val showDetailFragment = { svcid: String ->
        DetailFragment.newInstance(svcid)
            .show(requireActivity().supportFragmentManager, "Detail")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)

        val view = binding.root

        viewModel = ViewModelProvider(this, CategoryViewModel.factory).get(
            CategoryViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.reCategory.layoutManager = LinearLayoutManager(requireContext())

        categoryPrefRepository = CategoryPrefRepositoryImpl(requireContext())

        // ViewModel 초기화
        viewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)

        // 카테고리 아이템 클릭 이벤트 처리
        val onCategoryItemClick: (String) -> Unit = { svcid ->
            showDetailFragment
        }

        // LiveData 관찰
        viewModel.categories.observe(viewLifecycleOwner, Observer { categories ->
            // 카테고리 데이터와 클릭 리스너를 어댑터에 제공하여 리사이클러뷰에 표시
//            adapter = CategoryAdapter(categories, onCategoryItemClick)
            binding.reCategory.adapter = adapter
        })

        // 카테고리 데이터 가져오기
        viewModel.fetchCategories()
    }
}