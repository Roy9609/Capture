package com.roy.capturelib.ui.fragment

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import com.lygttpod.monitor.data.CaptureData
import com.roy.capture.utils.GsonHelper
import com.roy.capture.R


import com.roy.capturelib.bean.JsonBinder
import com.roy.capture.ui.adapter.CaptureListDetailsAdapter
import com.roy.capture.bean.CaptureHeaderBean
import com.roy.capture.bean.CaptureListDetailsBean

import com.roy.capture.ui.base.CaptureBaseFragment

import kotlin.concurrent.thread



class CaptureOverviewFragment : CaptureBaseFragment() {

  companion object{
    var type1=1 //总览
    var type2 =2 //请求
    var type3 = 3; //响应
  }

  var captureAdapter : CaptureListDetailsAdapter?=null

  var  list: ArrayList<CaptureListDetailsBean> = ArrayList()

  var data:CaptureData?=null

    override fun initView(view: View?) {

    }

    override fun initEvents() {
      var type =  arguments?.getInt("type")
      var jsonBinder =  arguments?.getBinder("jsonItem")as? JsonBinder
        data  = jsonBinder?.dataItemCurrentPosition

       when(type){
         type1 ->{
             getOverviewData()
         }
         type2 ->{
              getRequestData()
         }

         type3 ->{
              getResponseData()
         }
       }

        val rv = view?.findViewById<RecyclerView>(R.id.rv)
        captureAdapter = CaptureListDetailsAdapter(list)
        rv?.layoutManager = LinearLayoutManager(context)
        rv?.adapter = captureAdapter
        captureAdapter!!.setEmptyView(R.layout.capture_empty_layout)
    }

    //获取响应数据
    private fun getResponseData() {
        thread {
            data?.responseHeaders?.apply {
                var captureHeaders: ArrayList<CaptureHeaderBean?>? =
                        GsonHelper.fromJson(this, object : TypeToken<ArrayList<CaptureHeaderBean?>?>() {}.type)
                captureHeaders?.apply {
                    captureHeaders.forEachIndexed{ index, header ->
                        list.add(CaptureListDetailsBean().apply {
                            this.title = header?.name
                            this.value = header?.value
                            if(index==0) {
                                this.generalTitle = "Headers"
                            }
                            this.type = CaptureListDetailsAdapter.TYPE_1
                        })
                    }
                }
            }

            data?.responseBody?.let {
                list.add(CaptureListDetailsBean().apply {
                    this.generalTitle = "Body"
                    if(TextUtils.isEmpty(it)){
                        this.value = "--"
                    }else {
                        this.value = it
                    }
                    this.type = CaptureListDetailsAdapter.TYPE_2
                })
            }

            activity?.runOnUiThread{
                captureAdapter?.notifyDataSetChanged()
            }
        }




    }


    //获取请求数据
    private fun getRequestData() {

        data?.requestHeaders?.apply {
            var captureHeaders: ArrayList<CaptureHeaderBean?>? =
              GsonHelper.fromJson(this, object : TypeToken<ArrayList<CaptureHeaderBean?>?>() {}.type)
             captureHeaders?.apply {
                 captureHeaders.forEachIndexed{ index, header ->
                       list.add(CaptureListDetailsBean().apply {

                           this.title = header?.name
                           this.value = header?.value
                           if(index==0) {
                               this.generalTitle = "Headers"
                           }
                           this.type = CaptureListDetailsAdapter.TYPE_1
                       })
                 }
             }
        }

        if(!TextUtils.isEmpty(data?.requestContentType)){
            list.add(CaptureListDetailsBean().apply {
                this.title = "Content-Type"
                this.value = data?.requestContentType
                this.type = CaptureListDetailsAdapter.TYPE_1
            })
        }

        data?.requestBody?.let {
            list.add(CaptureListDetailsBean().apply {
                 this.generalTitle = "Body"
                  if(TextUtils.isEmpty(it)){
                      this.value = "--"
                  }else {
                      this.value = it
                  }
                this.type = CaptureListDetailsAdapter.TYPE_1
            })
        }

        captureAdapter?.notifyDataSetChanged()


    }

    //获取总览数据
    private fun getOverviewData() {
    if(!TextUtils.isEmpty(data?.url)){
        list.add(CaptureListDetailsBean().apply {
            this.title = "URL"
            this.value = data?.url
            this.type = CaptureListDetailsAdapter.TYPE_1
        })
    }
    if(!TextUtils.isEmpty(data?.method)){
        list.add(CaptureListDetailsBean().apply {
            this.title = "Method"
            this.value = data?.method
            this.type = CaptureListDetailsAdapter.TYPE_1
        })
    }


     list.add(CaptureListDetailsBean().apply {
                this.title = "Status"
                this.value = "${data?.responseCode}"
                this.type = CaptureListDetailsAdapter.TYPE_1
      })


    if(!TextUtils.isEmpty(data?.requestTime)){
        list.add(CaptureListDetailsBean().apply {
            this.title = "Request date"
            this.value = data?.requestTime
            this.type = CaptureListDetailsAdapter.TYPE_1
        })
    }

    if(!TextUtils.isEmpty(data?.responseTime)){
        list.add(CaptureListDetailsBean().apply {
            this.title = "Response date"
            this.value = data?.responseTime
            this.type = CaptureListDetailsAdapter.TYPE_1
        })
    }


      list.add(CaptureListDetailsBean().apply {
          this.title = "Time interval"
          this.value = "${defaultLong(data?.duration)/1000.toFloat()}"
          this.type = CaptureListDetailsAdapter.TYPE_1
      })

        captureAdapter?.notifyDataSetChanged()

  }

    fun   defaultLong(value:Long?):Long{
        return value?:0.toLong()
    }

  override fun initData() {

    }

    override fun inflateBaseLayoutRes(): Int {
          return  R.layout.capture_overview_fragment_layout
    }

    override fun initConfig(view: View?) {

    }

    override fun onDestroy() {
        super.onDestroy()
        captureAdapter?.destroy()
    }


}