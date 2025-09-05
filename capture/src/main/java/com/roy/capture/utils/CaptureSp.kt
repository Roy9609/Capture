package com.roy.capturelib.utils

import com.roy.capture.utils.CacheUtil


object CaptureSp {
    /**
     * 获取抓包是否打开
     * @param context Context
     * @return Boolean
     */
    fun getSwitchCapture():Boolean{
        return  CacheUtil.getBoolean("cap_switch_")
    }

    /**
     * 抓住抓包开关
     * @param context Context
     * @param value Boolean
     */
    fun setSwitchCapture( value:Boolean){
        CacheUtil.putBoolean("cap_switch_",value)
    }


}