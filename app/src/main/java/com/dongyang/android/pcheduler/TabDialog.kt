package com.dongyang.android.pcheduler

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.dongyang.android.pcheduler.databinding.DialogDetailBinding
import com.google.android.material.tabs.TabLayout

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-07
 * @Description :
 */

class TabDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 화면 밖을 누르면 dismiss 되게 설정
        isCancelable = true
    }

    private lateinit var dialogBinding : DialogDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialogBinding = DialogDetailBinding.inflate(inflater, container, false)
        return dialogBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeView(0, dialogBinding.dialogTabContainer)

        dialogBinding.dialogTabLayout
        dialogBinding.dialogTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {

                    0 -> {
                        changeView(0, dialogBinding.dialogTabContainer)
                        Log.d("My Dialog", tab.position.toString())
                    }
                    1 -> changeView(1, dialogBinding.dialogTabContainer)
                }
            }

        })
    }

    fun changeView(position : Int, container : ConstraintLayout) {

        Log.d("My Dialog", "get Change View")

        val inflater = layoutInflater

        if (container.childCount > 0) {
            container.removeViewAt(0)
        }

        var view : View? = null

        when (position) {
            0 -> view = inflater.inflate(R.layout.tab_set_date, container, false)
            1 -> view = inflater.inflate(R.layout.tab_set_notif, container, false)
        }

        if (view != null) {
            container.addView(view)
        }

        Log.d("My Dialog", position.toString())

    }

}