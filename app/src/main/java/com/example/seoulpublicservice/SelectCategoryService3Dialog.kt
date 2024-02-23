package com.example.seoulpublicservice

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.seoulpublicservice.databinding.DialogSelectCategoryService1Binding
import com.example.seoulpublicservice.databinding.DialogSelectCategoryService2Binding
import com.example.seoulpublicservice.databinding.DialogSelectCategoryService3Binding
import com.example.seoulpublicservice.databinding.DialogSelectCategoryService4Binding
import com.example.seoulpublicservice.databinding.DialogSelectCategoryService5Binding

class SelectCategoryService3Dialog: DialogFragment() {

    private var _binding: DialogSelectCategoryService3Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogSelectCategoryService3Binding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}