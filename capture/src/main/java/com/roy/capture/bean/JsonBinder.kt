package com.roy.capturelib.bean

import android.os.Binder
import com.lygttpod.monitor.data.CaptureData


data class JsonBinder constructor(var dataItemCurrentPosition:CaptureData?):Binder()