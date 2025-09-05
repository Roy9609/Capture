package com.roy.capturelib.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

import com.roy.capturelib.bean.CaptureSearchHistoryData


@Dao
interface CaptureSearchHistoryDataDao {

    @Insert
    fun insert(data: CaptureSearchHistoryData)

    @Query("DELETE FROM capture_search_history")
    fun deleteAll()

    @Query("SELECT * FROM capture_search_history ORDER BY id DESC LIMIT :limit OFFSET :offset")
    fun queryByOffsetForAndroid(limit: Int, offset: Int): LiveData<MutableList<CaptureSearchHistoryData>>

}