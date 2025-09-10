package com.roy.capture.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.roy.capture.bean.CaptureData
import com.roy.capture.utils.TIME_HOUR_MINUTES
import com.roy.capture.utils.formatData
import com.roy.capture.R
import com.roy.capture.databinding.CaptureItemCaptureListAdapterBinding

import com.roy.capture.ui.CaptureListDetailsActivity
import com.roy.capture.utils.FileSizeUtils
import java.util.*


class CaptureListAdapter(list: ArrayList<CaptureData>):BaseQuickAdapter<CaptureData, BaseViewHolder>(R.layout.capture_item_capture_list_adapter, list) {

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: CaptureData) {

        val dataBinding = DataBindingUtil.bind<CaptureItemCaptureListAdapterBinding>(holder.itemView)
        
        val clContent = dataBinding?.clContent
        val llLeft = dataBinding?.llLeft
        val tvTime = dataBinding?.tvTime
        val tvTimeConsuming = dataBinding?.tvTimeConsuming
        val tvInterfaceName = dataBinding?.tvInterfaceName
        val tvRequestStatus = dataBinding?.tvRequestStatus
        val tvSize =  dataBinding?.tvSize
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = defaultLong(item.requestTimeMillisecond) //转换为毫秒
        val date = calendar.time
        tvTime?.text = date.formatData(TIME_HOUR_MINUTES)
        tvTimeConsuming?.text = "${item.duration/1000f}"
        tvInterfaceName?.text = item.url
        tvRequestStatus?.text = "${item.responseCode}  ${item.method}  ${item.responseContentType}"
        tvSize?.text = "${FileSizeUtils.getLength(item.responseContentLength)}"

        if(item.responseCode == 200){
            llLeft?.setBackgroundColor(Color.parseColor("#02C185"))
        }else{
            llLeft?.setBackgroundColor(Color.parseColor("#D81B60"))
        }


        clContent?.setOnClickListener {
            CaptureListDetailsActivity.doIntent(context,item)
        }


    }

      private fun  defaultLong(value:Long?):Long{
        return value?:0.toLong()
    }
}