package com.roy.captureoperate

import android.content.Context
import android.content.Intent
import okhttp3.Interceptor

/**
 *    desc   :
 *    date   : 2025/9/5 09:06
 *    author : Roy
 *    version: 1.0
 */
class CaptureOperate private constructor() {


    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CaptureOperate()
        }
    }

    fun captureInterceptor(): Interceptor? {
        return commonReflection("captureInterceptor") as? Interceptor
    }



    //打开抓包页面
    fun startCaptureListActivity(context: Context){
        val intent =  Intent(context, Class.forName("com.roy.capture.ui.CaptureListActivity"))
        context.startActivity(intent)
    }



    //获取当前抓包开关打开或关闭
    fun getSwitchCapture(): Boolean {
        return  commonReflection("getSwitchCapture") as? Boolean ?:false
    }




    private fun commonReflection(methodName:String):Any?{
        try {
            val clazz = Class.forName("com.roy.capture.CaptureLib")
            val companionField = clazz.getDeclaredField("Companion")
            companionField.isAccessible = true
            val companionObj = companionField.get(null)
            val getInstanceMethod = companionObj::class.java.getDeclaredMethod("getInstance")
            getInstanceMethod.isAccessible = true
            val instance = getInstanceMethod.invoke(companionObj)

            val method = clazz.getDeclaredMethod(methodName)
            method.isAccessible = true

            return method.invoke(instance)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


}