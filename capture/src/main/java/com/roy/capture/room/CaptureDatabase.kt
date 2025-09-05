package com.roy.capture.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.roy.capture.bean.CaptureData
import com.roy.capturelib.bean.CaptureJsNativeInteractData
import com.roy.capturelib.bean.CaptureSearchHistoryData

@Database(entities = [CaptureData::class,
    CaptureSearchHistoryData::class,CaptureJsNativeInteractData::class], version = 4, exportSchema = false)
abstract class CaptureDatabase : RoomDatabase() {

    abstract fun captureDao(): CaptureDao

    abstract fun searchHistoryDao(): CaptureSearchHistoryDataDao

    abstract fun  jsNativeInteractDataDao(): CaptureJsNativeInteractDataDao

}