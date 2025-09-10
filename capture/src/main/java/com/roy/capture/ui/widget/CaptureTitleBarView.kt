package com.roy.capture.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.roy.capture.R
import com.roy.capture.databinding.CaptureTitleViewBinding
import com.roy.capture.utils.CaptureScreenUtils



class CaptureTitleBarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr){
    var activity: Activity?
    var clickListener: OnClickListener?=null
    var autoFinish = true;
    var binding:CaptureTitleViewBinding = CaptureTitleViewBinding.inflate(LayoutInflater.from(context),this,true)
    init {
        activity = context as Activity?
       // LayoutInflater.from(context).inflate(R.layout.capture_title_view, this)

        initView()
    }

    fun initView(){
        binding.tvBack.setOnClickListener {
            if(autoFinish) {
                activity?.finish()
            }
            clickListener?.onClick(it)
        }

        setIcon(binding.tvBack,R.drawable.capture_icon_back)
    }

    fun getLeftBackTextView():TextView{
        return binding.tvBack
    }

    fun setBackOnClick(clickListener: OnClickListener){
        this.autoFinish = false
        this.clickListener = clickListener
    }

    fun setIcon(text:TextView,icon:Int){
        var drawable =  ContextCompat.getDrawable(context,icon)

        drawable?.apply {
            var size = CaptureScreenUtils.dp2Px(context,25f).toInt()
            drawable!!.setBounds(0, 0, size, size)
            text.setCompoundDrawables(drawable,null,null,null)

        }
    }


    fun getTitleView():TextView{
        return binding.tvTitle
    }

    fun setTitle(str:String?){
        binding.tvTitle.setText(str)
    }

    fun getRightText3():TextView{
        return  binding.tvRight3
    }

    fun getRightText2():TextView{
        return  binding.tvRight2
    }

    fun getRightText1():TextView{
        return  binding.tvRight1
    }
}