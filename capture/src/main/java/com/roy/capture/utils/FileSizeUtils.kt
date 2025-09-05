package com.roy.capturelib.utils

import java.text.DecimalFormat


object FileSizeUtils {
    /**
     * 根据文件大小转换为B、KB、MB、GB单位字符串显示
     *
     * @param filesize 文件的大小（long型）
     * @return 返回 转换后带有单位的字符串
     */
    fun getLength(filesize: Long?): String? {
        if(filesize==null){
            return "0B";
        }
        var strFileSize: String? = null
        if (filesize < 1024) {
            strFileSize = filesize.toString() + "B"
            return strFileSize
        }
        val df = DecimalFormat("######0.00")
        strFileSize = if (filesize >= 1024 && filesize < 1024 * 1024) { //KB
            df.format(filesize.toDouble() / 1024) + "KB"
        } else if (filesize >= 1024 * 1024 && filesize < 1024 * 1024 * 1024) { //MB
            df.format(filesize.toDouble() / (1024 * 1024)) + "MB"
        } else { //GB
            df.format(filesize.toDouble() / (1024 * 1024 * 1024)) + "GB"
        }
        return strFileSize
    }
}