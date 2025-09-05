package com.roy.capture.utils

import android.content.Context
import android.os.Parcelable

import com.tencent.mmkv.MMKV


object CacheUtil {
    private lateinit var mmkv: MMKV

    /**
     * 初始化
     * @param context Context
     * @param isMultiProcess Boolean 是否需要在多个进程中共享数据
     */
    @JvmOverloads
    fun init(context: Context, isMultiProcess: Boolean = false) {
        MMKV.initialize(context)
        val processMode = if (isMultiProcess) MMKV.MULTI_PROCESS_MODE else MMKV.SINGLE_PROCESS_MODE
        mmkv = MMKV.defaultMMKV(processMode, null)
    }

    @JvmOverloads
    fun putBoolean(key: String?, value: Boolean, keepCache: Boolean = false): Boolean {
        keepCache(key, keepCache)
        return mmkv.encode(key, value)
    }

    @JvmOverloads
    fun getBoolean(key: String?, defaultValue: Boolean = false): Boolean {
        return mmkv.decodeBool(key, defaultValue)
    }

    @JvmOverloads
    fun putInt(key: String?, value: Int, keepCache: Boolean = false): Boolean {
        keepCache(key, keepCache)
        return mmkv.encode(key, value)
    }

    @JvmOverloads
    fun getInt(key: String?, defaultValue: Int = 0): Int {
        return mmkv.decodeInt(key, defaultValue)
    }

    @JvmOverloads
    fun putLong(key: String?, value: Long, keepCache: Boolean = false): Boolean {
        keepCache(key, keepCache)
        return mmkv.encode(key, value)
    }

    @JvmOverloads
    fun getLong(key: String?, defaultValue: Long = 0): Long {
        return mmkv.decodeLong(key, defaultValue)
    }

    @JvmOverloads
    fun putFloat(key: String?, value: Float, keepCache: Boolean = false): Boolean {
        keepCache(key, keepCache)
        return mmkv.encode(key, value)
    }

    @JvmOverloads
    fun getFloat(key: String?, defaultValue: Float = 0f): Float {
        return mmkv.decodeFloat(key, defaultValue)
    }

    @JvmOverloads
    fun putDouble(key: String?, value: Double, keepCache: Boolean = false): Boolean {
        keepCache(key, keepCache)
        return mmkv.encode(key, value)
    }

    @JvmOverloads
    fun getDouble(key: String?, defaultValue: Double = 0.0): Double {
        return mmkv.decodeDouble(key, defaultValue)
    }

    @JvmOverloads
    fun putString(key: String?, value: String?, keepCache: Boolean = false): Boolean {
        keepCache(key, keepCache)
        return mmkv.encode(key, value)
    }

    @JvmOverloads
    fun getString(key: String?, defaultValue: String? = null): String? {
        return mmkv.decodeString(key, defaultValue)
    }

    @JvmOverloads
    fun putStringSet(key: String?, value: Set<String?>?, keepCache: Boolean = false): Boolean {
        keepCache(key, keepCache)
        return mmkv.encode(key, value)
    }

    @JvmOverloads
    fun getStringSet(key: String?, defaultValue: Set<String?>? = null): Set<String?>? {
        return mmkv.decodeStringSet(key, defaultValue)
    }

    @JvmOverloads
    fun putByteArray(key: String?, value: ByteArray?, keepCache: Boolean = false): Boolean {
        keepCache(key, keepCache)
        return mmkv.encode(key, value)
    }

    @JvmOverloads
    fun getByteArray(key: String?, defaultValue: ByteArray? = null): ByteArray? {
        return mmkv.decodeBytes(key, defaultValue)
    }

    @JvmOverloads
    fun putParcelable(key: String?, value: Parcelable?, keepCache: Boolean = false): Boolean {
        keepCache(key, keepCache)
        return mmkv.encode(key, value)
    }

    @JvmOverloads
    fun <T : Parcelable?> getParcelable(
        key: String?,
        tClass: Class<T>?,
        defaultValue: T? = null
    ): T? {
        return mmkv.decodeParcelable(key, tClass, defaultValue)
    }


    @JvmOverloads
    fun putList(key: String?, value: List<Any?>?, keepCache: Boolean = false) {
        value?.let {
            keepCache(key, keepCache)
            putString(key, GsonHelper.toJson(value))
        }
    }

    @JvmOverloads
    fun <T> getList(key: String?, tClass: Class<T>, defaultValue: T? = null): T? {
        return getString(key)?.let {
            GsonHelper.fromJson(it, tClass)
        } ?: defaultValue
    }

    /**
     * 移除某个key对
     *
     * @param key
     */
    fun remove(key: String?) {
        KeepCache.remove(key)
        mmkv.removeValueForKey(key)
    }

    /**
     * 清除所有key
     */
    fun clearAll() {
        mmkv.allKeys()?.forEach {
            if (!KeepCache.contains(it)) {
                remove(it)
            }
        }
    }

    /**
     * 清除缓存的时候是否保留该缓存
     * @param key
     * @param keepCache
     */
    private fun keepCache(key: String?, keepCache: Boolean) {
        if (keepCache) {
            KeepCache.keep(key)
        }
    }

    /**
     * 清除缓存时，某些缓存必须保留，如token、应用资源，增加KeepCache类处理相关场景
     */
    object KeepCache {
        private const val KEY_KEPP_CACHE = "KEY_KEPP_CACHE"

        /**
         * 清除缓存的时候是否保留该缓存
         * @param key
         */
        fun keep(key: String?) {
            getCacheSet()?.let {
                it.add(key)
                putStringSet(KEY_KEPP_CACHE, it)
            }
        }

        fun contains(key: String?): Boolean {
            if (key == KEY_KEPP_CACHE) {
                return true
            }
            return getCacheSet()?.contains(key) ?: false
        }

        fun remove(key: String?) {
            getCacheSet()?.let {
                if (it.contains(key)) {
                    it.remove(key)
                    putStringSet(KEY_KEPP_CACHE, it)
                }
            }
        }

        private fun getCacheSet(): MutableSet<String?>? {
            return getStringSet(KEY_KEPP_CACHE, mutableSetOf())?.toMutableSet()
        }
    }

}