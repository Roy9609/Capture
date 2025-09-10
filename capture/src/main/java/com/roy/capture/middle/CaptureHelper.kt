package com.roy.capture.middle

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.roy.capture.bean.CaptureData
import com.roy.capture.room.CaptureDao
import com.roy.capture.room.CaptureDatabase
import com.roy.capture.CaptureLib
import com.roy.capture.bean.CaptureJsNativeInteractData
import com.roy.capture.bean.CaptureSearchHistoryData
import com.roy.capture.room.CaptureJsNativeInteractDataDao
import com.roy.capture.room.CaptureSearchHistoryDataDao
import kotlin.concurrent.thread


class CaptureHelper {

    var captureDb: CaptureDatabase? = null
     var context: Context?=null

    companion object{

        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
            CaptureHelper()
        }
    }

    fun init(context: Context){
        this.context = context
        initCaptureDataDao(context,"capture-db.db")
    }


    private fun initCaptureDataDao(context: Context, dbName: String) {
        if (captureDb == null) {
            captureDb = Room
                    .databaseBuilder(context.applicationContext, CaptureDatabase::class.java, dbName)
                    .fallbackToDestructiveMigration()
                    .build()
         }
    }

    private fun getCaptureDataDao(): CaptureDao? {
        return captureDb?.captureDao()
    }


    private fun getCaptureSearchHistoryDataDao(): CaptureSearchHistoryDataDao? {
        return captureDb?.searchHistoryDao()
    }

    private fun getJsNativeInteractDataDao(): CaptureJsNativeInteractDataDao?{
        return  captureDb?.jsNativeInteractDataDao()
    }


    fun insert(captureData: CaptureData) {
        thread {
            getCaptureDataDao()?.insert(captureData)
        }
    }


    fun update(captureData: CaptureData) {
        thread {
            getCaptureDataDao()?.update(captureData)
        }
    }

    fun deleteAll() {
        thread {
            getCaptureDataDao()?.deleteAll()
        }
    }


    /**
     *
     * @param limit Int   表示要提取的数量
     * @param offset Int   表示第几行
     */
    fun queryByOffsetForAndroid(limit: Int, offset: Int): LiveData<MutableList<CaptureData>>? {
       return  getCaptureDataDao()?.queryByOffsetForAndroid(limit,offset)
    }

    /**
     * 查询
     * @param str String?
     * @return LiveData<MutableList<MonitorData>>?
     */
    fun queryLikeUrl(str:String?):LiveData<MutableList<CaptureData>>?{
        return getCaptureDataDao()?.queryLikeData(str)
    }


    /**
     * 保存 h5任意门 功能 搜索历史数据
     * @param history CaptureSearchHistoryData
     */
    fun searchHistoryInsert(history: CaptureSearchHistoryData){
        thread {
            getCaptureSearchHistoryDataDao()?.insert(history)
        }

    }

    /**
     * 获取 h5任意门 功能 数据
     * @param limit Int
     * @param offset Int
     * @return LiveData<MutableList<CaptureSearchHistoryData>>?
     */
    fun searchHistoryQuery(limit: Int, offset: Int): LiveData<MutableList<CaptureSearchHistoryData>>? {
       return  getCaptureSearchHistoryDataDao()?.queryByOffsetForAndroid(limit,offset)
    }


    /**
     * 清除 h5任意门 功能 数据
     */
    fun searchHistoryDeleteAll(){
        thread {
            getCaptureSearchHistoryDataDao()?.deleteAll()
        }

    }



    /**
     *  查询js交互日志
     * @param limit Int   表示要提取的数量
     * @param offset Int   表示第几行
     */
    fun queryJsNativeByOffsetForAndroid(limit: Int, offset: Int): LiveData<MutableList<CaptureJsNativeInteractData>>? {
        return  getJsNativeInteractDataDao()?.queryByOffsetForAndroid(limit,offset)
    }

    /**
     * 查询 js交互日志
     * @param str String?
     * @return LiveData<MutableList<MonitorData>>?
     */
    fun queryJsNativeLikeUrl(str:String?):LiveData<MutableList<CaptureJsNativeInteractData>>?{
        return getJsNativeInteractDataDao()?.queryLikeData(str)
    }

    /**
     * 删除 js交互日志
     */
    fun deleteJsNativeAll() {
        thread {
            getJsNativeInteractDataDao()?.deleteAll()
        }
    }

    /**
     * 插入 js交互日志
     * eInteractData
     */
    fun insertJsNative(content:String?) {
        context?.apply {
            if(CaptureLib.instance.getSwitchCapture()){
                thread {
                    try {
                        getJsNativeInteractDataDao()?.insert(CaptureJsNativeInteractData().apply {
                            this.content = content
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        }

    }

}