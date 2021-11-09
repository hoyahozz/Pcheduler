package com.dongyang.android.pcheduler

import android.os.Build
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.dongyang.android.pcheduler.databinding.BottomsheetDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-09
 * @Description : 할 일을 눌렀을 때 나오는 바텀시트 클래스
 */

class DetailBottomSheet(content: String) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetDetailBinding
    private val content = content

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dbsEtContent.setText(content)

        val bottomSheet: ViewGroup = binding.dbsContainer


        binding.dbsBtnStartDate.setOnClickListener {

            if (binding.dbsDatePicker.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(bottomSheet)
                binding.dbsDatePicker.visibility = View.GONE
            } else {
                TransitionManager.beginDelayedTransition(bottomSheet)
                binding.dbsDatePicker.visibility = View.VISIBLE
            }
        }
    }


}