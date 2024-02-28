package com.example.seoulpublicservice.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.seoulpublicservice.SeoulPublicServiceApplication
import com.example.seoulpublicservice.databinding.FragmentMyPageBinding
import com.example.seoulpublicservice.seoul.Row
import kotlin.random.Random

class MyPageFragment : Fragment() {

    companion object {
        fun newInstance() = MyPageFragment()
    }

    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyPageViewModel by viewModels()

    private val myPageSavedAdapter by lazy {
        MyPageSavedAdapter()
            .apply {
                val rows = (requireActivity().application as SeoulPublicServiceApplication).rowList
                if (rows.isEmpty()) {
                    var a = 0
                    submitList(
                        listOf(
                            Row.new(svcnm = "첫 번째 제목~~", areanm = "가가구", svcstatnm = "접수${++a}"),
                            Row.new(svcnm = "두 번째 제목~~", areanm = "나나구", svcstatnm = "접수${++a}"),
                            Row.new(svcnm = "세 번째 제목~~", areanm = "다다구", svcstatnm = "접수${++a}"),
                            Row.new(svcnm = "첫 번째 제목~~", areanm = "가가구", svcstatnm = "접수${++a}"),
                            Row.new(svcnm = "두 번째 제목~~", areanm = "나나구", svcstatnm = "접수${++a}"),
                            Row.new(svcnm = "세 번째 제목~~", areanm = "다다구", svcstatnm = "접수${++a}"),
                            Row.new(svcnm = "첫 번째 제목~~", areanm = "가가구", svcstatnm = "접수${++a}"),
                            Row.new(svcnm = "두 번째 제목~~", areanm = "나나구", svcstatnm = "접수${++a}"),
                            Row.new(svcnm = "세 번째 제목~~", areanm = "다다구", svcstatnm = "접수${++a}"),
                        )
                    )
                } else {
                    val random = Random
                    submitList(List(9) { rows[random.nextInt(rows.size)] })
                }
            }
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
        MyPageAdapter().apply {
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

    private fun initViewModel() {
    }

}
