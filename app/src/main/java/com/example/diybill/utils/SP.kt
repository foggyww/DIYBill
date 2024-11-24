package com.example.diybill.utils

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.diybill.App
import com.tencent.mmkv.MMKV

// 键值对数据库 （基于 MMKV）
// 可以当作 SharedPreferences 使用
// 无需调用 apply() 方法
// 默认 KV 数据库
val SP = SPP()

class SPP(spName: String = "hustBill") {
    private val kv: MMKV = MMKV.mmkvWithID(spName, MMKV.MULTI_PROCESS_MODE)

    init {
        migration(spName)
    }

    private fun migration(name: String) {
        val oldMan: SharedPreferences = App.CONTEXT.getSharedPreferences(name, MODE_PRIVATE)
        kv.importFromSharedPreferences(oldMan)
        oldMan.edit().clear().commit()
    }

    fun clear() {
        kv.clearAll()
    }

    fun get(
        name: String, default: String? = null
    ) = kv.getString(name, default)

    fun getBytes(
        name: String,default: ByteArray? = null
    ): ByteArray? = kv.getBytes(name,default)

    fun getInt(
        name: String, default: Int = -1
    ) = kv.getInt(name, default)

    fun getBoolean(
        name: String, default: Boolean = false
    ) = kv.getBoolean(name, default)

    fun getLong(
        name: String, default: Long = 0
    ) = kv.getLong(name, default)

    fun set(
        name: String, value: String?
    ) {
        kv.edit().apply {
            if (value != null) putString(name, value)
            else remove(name)
        }.apply()
    }

    fun setBytes(
        name: String,value: ByteArray?
    ){
        if(value!=null) kv.putBytes(name,value)
        else remove(name)
    }

    fun setInt(
        name: String, value: Int?
    ) {
        kv.edit().apply {
            if (value != null) putInt(name, value)
            else remove(name)
        }.apply()
    }

    fun set(
        vararg pair: Pair<String, String?>
    ) {
        kv.edit().apply {
            pair.forEach {
                if (it.second != null) putString(it.first, it.second)
                else remove(it.first)
            }
        }.apply()
    }

    fun setBoolean(
        name: String, value: Boolean?
    ) {
        kv.edit().apply {
            if (value != null) putBoolean(name, value)
            else remove(name)
        }.apply()
    }

    fun setLong(
        name: String, value: Long?
    ) {
        kv.edit().apply {
            if (value != null) putLong(name, value)
            else remove(name)
        }.apply()
    }

    fun remove(vararg name: String) {
        kv.edit().apply {
            name.forEach {
                remove(it)
            }
        }.apply()
    }
}


