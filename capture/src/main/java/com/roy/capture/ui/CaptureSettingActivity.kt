package com.roy.capture.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.roy.capture.R
import com.roy.capture.databinding.CaptureSettingActivityBinding
import com.roy.capture.CaptureLib
import com.roy.capture.ui.widget.CaptureSwitchButton


class CaptureSettingActivity:AppCompatActivity() {

    companion object{
        fun doIntent(context:Context){

            context.startActivity(Intent(context,CaptureSettingActivity::class.java))

        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

      val binding =  DataBindingUtil.setContentView<CaptureSettingActivityBinding>(this,R.layout.capture_setting_activity)

        binding.ctvTitle.getTitleView().setText("设置")
        val capSwitch =  CaptureLib.instance.getSwitchCapture()
        binding.csb.isChecked = capSwitch

        binding.csb.setOnCheckedChangeListener(object : CaptureSwitchButton.OnCheckedChangeListener{
            override fun onCheckedChanged(view: CaptureSwitchButton?, isChecked: Boolean) {
                 CaptureLib.instance.setSwitchCapture(isChecked)
            }

        })
    }
}