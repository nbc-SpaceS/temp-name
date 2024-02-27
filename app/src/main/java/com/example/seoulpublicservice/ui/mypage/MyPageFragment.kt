package com.example.seoulpublicservice.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.seoulpublicservice.databinding.FragmentMyPageBinding
import com.example.seoulpublicservice.seoul.Row

class MyPageFragment : Fragment() {

    companion object {
        fun newInstance() = MyPageFragment()
    }

    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyPageViewModel by viewModels()

    private val myPageSavedAdapter = MyPageSavedAdapter().apply {
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
    }
    private val myPageAdapter = MyPageAdapter().apply {
        var a = 0
        submitList(
            listOf(
                MyPageAdapter.MultiView.Profile,
                MyPageAdapter.MultiView.Saved(myPageSavedAdapter),
                MyPageAdapter.MultiView.ReviewedHeader
            )
                    + listOf(
                MyPageAdapter.MultiView.Reviewed(Row.new(svcnm = "${++a} 번째 제목~~", areanm = "가가구")),
                MyPageAdapter.MultiView.Reviewed(Row.new(svcnm = "${++a} 번째 제목~~", areanm = "나나구")),
                MyPageAdapter.MultiView.Reviewed(Row.new(svcnm = "${++a} 번째 제목~~", areanm = "다다구")),
                MyPageAdapter.MultiView.Reviewed(Row.new(svcnm = "${++a} 번째 제목~~", areanm = "가가구")),
                MyPageAdapter.MultiView.Reviewed(Row.new(svcnm = "${++a} 번째 제목~~", areanm = "나나구")),
                MyPageAdapter.MultiView.Reviewed(Row.new(svcnm = "${++a} 번째 제목~~", areanm = "다다구")),
                MyPageAdapter.MultiView.Reviewed(Row.new(svcnm = "${++a} 번째 제목~~", areanm = "가가구")),
                MyPageAdapter.MultiView.Reviewed(Row.new(svcnm = "${++a} 번째 제목~~", areanm = "나나구")),
                MyPageAdapter.MultiView.Reviewed(Row.new(svcnm = "${++a} 번째 제목~~", areanm = "다다구")),
            )
        )
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
