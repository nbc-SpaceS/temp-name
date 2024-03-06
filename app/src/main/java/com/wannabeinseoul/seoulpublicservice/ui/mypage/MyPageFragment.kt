package com.wannabeinseoul.seoulpublicservice.ui.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentMyPageBinding
import com.wannabeinseoul.seoulpublicservice.detail.DetailFragment
import com.wannabeinseoul.seoulpublicservice.seoul.Row
import kotlin.random.Random

class MyPageFragment : Fragment() {

    companion object {
        fun newInstance() = MyPageFragment()
    }

    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyPageViewModel by viewModels { MyPageViewModel.factory }

    private val showDetailFragment = { svcid: String ->
        DetailFragment.newInstance(svcid)
            .show(requireActivity().supportFragmentManager, "Detail")
    }

    private val myPageSavedAdapter by lazy {
        MyPageSavedAdapter { svcid -> showDetailFragment(svcid) }
    }

    private val fixedItems: List<MyPageAdapter.MultiView> by lazy {
        listOf(
            MyPageAdapter.MultiView.Profile {
                EditProfileDialog.newInstance()
                    .show(requireActivity().supportFragmentManager, "EditProfileDialog")
            },
            MyPageAdapter.MultiView.Saved(myPageSavedAdapter),
            MyPageAdapter.MultiView.ReviewedHeader
        )
    }

    private val myPageAdapter by lazy {
        MyPageAdapter(
            onClearClick = { viewModel.clearSavedList() },
            onReviewedClick = showDetailFragment,
        ).apply {
            val rows = (requireActivity().application as SeoulPublicServiceApplication).rowList
            if (rows.isEmpty()) {
                var a = 0
                submitList(
                    fixedItems + listOf(
                        MyPageAdapter.MultiView
                            .Reviewed(Row.new(svcnm = "${++a} 번째 제목~~", areanm = "가가구")),
                        MyPageAdapter.MultiView
                            .Reviewed(Row.new(svcnm = "${++a} 번째 제목~~", areanm = "나나구")),
                        MyPageAdapter.MultiView
                            .Reviewed(Row.new(svcnm = "${++a} 번째 제목~~", areanm = "다다구")),
                        MyPageAdapter.MultiView
                            .Reviewed(Row.new(svcnm = "${++a} 번째 제목~~", areanm = "가가구")),
                        MyPageAdapter.MultiView
                            .Reviewed(Row.new(svcnm = "${++a} 번째 제목~~", areanm = "나나구")),
                        MyPageAdapter.MultiView
                            .Reviewed(Row.new(svcnm = "${++a} 번째 제목~~", areanm = "다다구")),
                        MyPageAdapter.MultiView
                            .Reviewed(Row.new(svcnm = "${++a} 번째 제목~~", areanm = "가가구")),
                        MyPageAdapter.MultiView
                            .Reviewed(Row.new(svcnm = "${++a} 번째 제목~~", areanm = "나나구")),
                        MyPageAdapter.MultiView
                            .Reviewed(Row.new(svcnm = "${++a} 번째 제목~~", areanm = "다다구")),
                    )
                )
            } else {
                val random = Random
                submitList(fixedItems + List(9) {
                    MyPageAdapter.MultiView.Reviewed(rows[random.nextInt(rows.size)])
                })
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() = binding.let { b ->
        b.rvMyPage.adapter = myPageAdapter
    }

    private fun initViewModel() = viewModel.let { vm ->
        (requireActivity().application as SeoulPublicServiceApplication).container
            .savedPrefRepository.savedSvcidListLiveData.observe(viewLifecycleOwner) {
                Log.d(
                    "jj-마이페이지 프래그먼트",
                    "옵저버:savedPrefRepository.savedSvcidListLiveData ${it.toString().take(255)}"
                )
                vm.loadSavedList(it)
            }
        vm.savedList.observe(viewLifecycleOwner) {
            Log.d("jj-마이페이지 프래그먼트", "옵저버:savedList ${it.toString().take(255)}")
            myPageSavedAdapter.submitList(it)
        }
    }
}
