package com.roy.capturelib.ui.adapter

import android.graphics.Color
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.roy.capture.bean.CaptureData
import com.roy.capture.utils.TIME_HOUR_MINUTES
import com.roy.capture.utils.formatData
import com.roy.capture.R

import com.roy.capture.ui.CaptureListDetailsActivity
import com.roy.capturelib.utils.FileSizeUtils
import java.util.*


class CaptureListAdapter constructor(list: ArrayList<CaptureData>):BaseQuickAdapter<CaptureData, BaseViewHolder>(R.layout.capture_item_capture_list_adapter, list) {

    override fun convert(holder: BaseViewHolder, item: CaptureData) {
        var cl_content = holder.getView<ConstraintLayout>(R.id.cl_content)
        var ll_left = holder.getView<LinearLayout>(R.id.ll_left)
        var tvTime = holder.getView<TextView>(R.id.tv_time);
        var tv_time_consuming = holder.getView<TextView>(R.id.tv_time_consuming)
        var tv_interface_name = holder.getView<TextView>(R.id.tv_interface_name)
        var tv_request_status = holder.getView<TextView>(R.id.tv_request_status)
        var tv_size =  holder.getView<TextView>(R.id.tv_size)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = defaultLong(item.requestTimeMillisecond) //转换为毫秒
        val date = calendar.time
        tvTime.setText(date.formatData(TIME_HOUR_MINUTES))
        tv_time_consuming.setText("${item.duration/1000f}")
        tv_interface_name.setText(item.url)
        tv_request_status.setText("${item.responseCode}  ${item.method}  ${item.responseContentType}")
        tv_size.setText("${FileSizeUtils.getLength(item.responseContentLength)}")

        if(item.responseCode == 200){
            ll_left.setBackgroundColor(Color.parseColor("#02C185"))
        }else{
            ll_left.setBackgroundColor(Color.parseColor("#D81B60"))
        }


        cl_content.setOnClickListener {
            CaptureListDetailsActivity.doIntent(context,item)
        }


    }

      fun  defaultLong(value:Long?):Long{
        return value?:0.toLong()
    }
}