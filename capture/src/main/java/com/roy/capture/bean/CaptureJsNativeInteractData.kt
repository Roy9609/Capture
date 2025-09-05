package com.roy.capturelib.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "js_native_interact")
class CaptureJsNativeInteractData {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "content")
    var content:String?=null

}