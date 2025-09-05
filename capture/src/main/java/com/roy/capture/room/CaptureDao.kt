package com.lygttpod.monitor.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lygttpod.monitor.data.CaptureData

@Dao
interface CaptureDao {
    @Query("SELECT * FROM capture WHERE id > :lastId ORDER BY id DESC")
    fun queryByLastIdForAndroid(lastId: Long): LiveData<MutableList<CaptureData>>

    @Query("SELECT * FROM capture ORDER BY id DESC LIMIT :limit OFFSET :offset")
    fun queryByOffsetForAndroid(limit: Int, offset: Int): LiveData<MutableList<CaptureData>>

    @Query("SELECT * FROM capture")
    fun queryAllForAndroid(): LiveData<MutableList<CaptureData>>

    @Query("SELECT * FROM capture WHERE id > :lastId ORDER BY id DESC")
    fun queryByLastId(lastId: Long): MutableList<CaptureData>

    @Query("SELECT * FROM capture ORDER BY id DESC LIMIT :limit OFFSET :offset")
    fun queryByOffset(limit: Int, offset: Int): MutableList<CaptureData>

    @Query("SELECT * FROM capture")
    fun queryAll(): MutableList<CaptureData>

    @Insert
    fun insert(data: CaptureData)

    @Update
    fun update(data: CaptureData)

    @Query("DELETE FROM capture")
    fun deleteAll()

    @Query("SELECT * FROM capture WHERE url LIKE  '%' || :str || '%'  ORDER BY id DESC")
    fun queryLikeData(str:String?):LiveData<MutableList<CaptureData>>

}