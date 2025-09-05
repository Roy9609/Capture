package com.roy.capturelib.bean

import android.os.Binder
import com.roy.capture.bean.CaptureData


data class JsonBinder constructor(var dataItemCurrentPosition: CaptureData?):Binder()