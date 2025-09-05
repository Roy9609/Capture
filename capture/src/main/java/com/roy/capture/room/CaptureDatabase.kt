package com.lygttpod.monitor.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lygttpod.monitor.data.CaptureData
import com.roy.capturelib.bean.CaptureJsNativeInteractData
import com.roy.capturelib.bean.CaptureSearchHistoryData
import com.roy.capturelib.room.CaptureJsNativeInteractDataDao
import com.roy.capturelib.room.CaptureSearchHistoryDataDao

@Database(entities = [CaptureData::class,
    CaptureSearchHistoryData::class,CaptureJsNativeInteractData::class], version = 4, exportSchema = false)
abstract class CaptureDatabase : RoomDatabase() {

    abstract fun captureDao(): CaptureDao

    abstract fun searchHistoryDao():CaptureSearchHistoryDataDao

    abstract fun  jsNativeInteractDataDao():CaptureJsNativeInteractDataDao

}