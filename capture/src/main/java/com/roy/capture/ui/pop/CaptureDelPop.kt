package com.roy.capturelib.ui.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.roy.capture.R
import com.roy.capture.databinding.CaptureDelPopBinding
import com.roy.capture.CaptureLib


import razerdp.basepopup.BasePopupWindow
import razerdp.util.animation.AnimationHelper
import razerdp.util.animation.TranslationConfig



class CaptureDelPop constructor(context: Context):BasePopupWindow(context)  {


    lateinit var tvDelOut: TextView
    private lateinit var tvCancel: TextView

    init {
        contentView =   DataBindingUtil.bind<CaptureDelPopBinding>(createPopupById(R.layout.capture_del_pop))!!.root
    }




    override fun onViewCreated(contentView: View) {
        super.onViewCreated(contentView)
        setOutSideTouchable(true)
        setOutSideDismiss(true)
        setPopupGravity(Gravity.BOTTOM)
        tvDelOut = contentView.findViewById(R.id.tv_login_out)
        tvCancel = contentView.findViewById(R.id.tv_cancel)

        tvDelOut.setOnClickListener {
            CaptureLib.instance.getCaptureHelper().deleteAll() //删除所有数据
            dismiss()
        }

        dismiss()
        tvCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateShowAnimation(): Animation? {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.FROM_BOTTOM)
                .toShow()
    }

    override fun onCreateDismissAnimation(): Animation? {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.TO_BOTTOM)
                .toShow()
    }






}