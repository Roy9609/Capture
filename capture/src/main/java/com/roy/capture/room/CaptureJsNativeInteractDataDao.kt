package com.roy.capturelib.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lygttpod.monitor.data.CaptureData
import com.roy.capturelib.bean.CaptureJsNativeInteractData
import com.roy.capturelib.bean.CaptureSearchHistoryData


@Dao
interface CaptureJsNativeInteractDataDao {

    @Insert
    fun insert(data:CaptureJsNativeInteractData)

    @Query("DELETE FROM js_native_interact")
    fun deleteAll()

    @Query("SELECT * FROM js_native_interact ORDER BY id DESC LIMIT :limit OFFSET :offset")
    fun queryByOffsetForAndroid(limit: Int, offset: Int): LiveData<MutableList<CaptureJsNativeInteractData>>

    @Query("SELECT * FROM js_native_interact WHERE content LIKE  '%' || :str || '%'  ORDER BY id DESC")
    fun queryLikeData(str:String?):LiveData<MutableList<CaptureJsNativeInteractData>>

}