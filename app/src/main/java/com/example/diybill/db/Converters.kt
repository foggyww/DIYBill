package com.example.diybill.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PicUrlsConverters{
    
    @TypeConverter
    fun stringToObject(value:String):List<String>{
        val listType = object : TypeToken<List<String>>() {
        }.type
        return Gson().fromJson(value,listType)
    }

    @TypeConverter
    fun objectToString(list: List<Any>):String{
        val gson = Gson()
        return gson.toJson(list)
    }
    
}