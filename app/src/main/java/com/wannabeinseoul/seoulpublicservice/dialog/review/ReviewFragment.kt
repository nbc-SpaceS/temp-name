package com.wannabeinseoul.seoulpublicservice.dialog.review

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentReviewBinding

private const val REVIEW_PARAM = "review_param"

class ReviewFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReviewViewModel by viewModels { ReviewViewModel.factory }

    private val adapter: ReviewAdapter by lazy {
        ReviewAdapter()
    }

    private var svcId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = com.wannabeinseoul.seoulpublicservice.databinding.FragmentReviewBinding.inflate(inflater, container, false)
        arguments?.let {
            svcId = it.getString(REVIEW_PARAM).toString()
        }
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.behavior.isDraggable = false
        return bottomSheetDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {

        rvReviewList.adapter = adapter

        ivReviewSendBtn.setOnClickListener {
            viewModel.uploadReview(svcId, etReviewInputField.text.toString())
            setInitialState()
        }

        etReviewInputField.setOnEditorActionListener { _, action, _ ->
            if (action == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.uploadReview(svcId, etReviewInputField.text.toString())
                setInitialState()
            }

            false
        }
    }

    private fun initViewModel() = with(viewModel) {
        setReviews(svcId)

        uiState.observe(viewLifecycleOwner) {
            adapter.submitList(it.toList())
            binding.tvReviewCount.text = it.size.toString()
            if (it.isNotEmpty()) {
                binding.tvReviewCount.isVisible = true
            }
        }
    }

    private fun setInitialState() {
        binding.etReviewInputField.setText("")
        binding.etReviewInputField.clearFocus()

        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            binding.etReviewInputField.windowToken,
            0
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(serviceID: String) =
            ReviewFragment().apply {
                arguments = Bundle().apply {
                    putString(REVIEW_PARAM, serviceID)
                }
            }
    }
}