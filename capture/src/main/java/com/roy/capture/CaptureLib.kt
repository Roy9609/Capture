package com.roy.capture


import android.app.Application
import com.lygttpod.monitor.interceptor.CaptureInterceptor
import com.roy.capturelib.middle.CaptureHelper
import com.roy.capturelib.utils.CaptureSp
import com.roy.capture.utils.CacheUtil


class CaptureLib private constructor() {


    private var application: Application? = null

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CaptureLib()
        }
    }

    //获取拦截器   其他地方会反射这个方法
    fun captureInterceptor(): CaptureInterceptor {
        return CaptureInterceptor()
    }


    fun init(application: Application) {
       this.application?:apply {
           this.application = application
           CacheUtil.init(application.applicationContext)
           CaptureHelper.instance.init(application)
       }

    }


    /**
     * 数据库操作类
     * @return CaptureHelper
     */
    fun getCaptureHelper(): CaptureHelper {
        return CaptureHelper.instance
    }


    /**
     * 获取抓包是否打开
     * @return Boolean
     */
    fun getSwitchCapture(): Boolean {
        return CaptureSp.getSwitchCapture()
    }

    /**
     * 抓住抓包开关    这个方法被其他地方反射
     * @param value Boolean
     */
    fun setSwitchCapture( value: Boolean) {
        CaptureSp.setSwitchCapture(value)
    }


}