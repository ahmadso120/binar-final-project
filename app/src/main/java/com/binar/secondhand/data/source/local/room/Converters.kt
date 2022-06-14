package com.binar.secondhand.data.source.local.room

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.binar.secondhand.data.source.local.entity.CategoryEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class Converters {
//    @TypeConverter
//    fun categoryListToJsonString(value: List<CategoryEntity>?): String = Gson().toJson(value)
//
//    @TypeConverter
//    fun jsonStringToCategoryList(value: String) =
//        Gson().fromJson(value, Array<CategoryEntity>::class.java).toList()

//    @TypeConverter
//    fun stringToListServer(data: String?): List<CategoryEntity?>? {
//        if (data == null) {
//            return Collections.emptyList()
//        }
//        val listType: Type = object : TypeToken<List<CategoryEntity?>?>() {}.type
//        return Gson().fromJson<List<CategoryEntity?>>(data, listType)
//    }
//
//    @TypeConverter
//    fun listServerToString(someObjects: List<CategoryEntity?>?): String? {
//        return Gson().toJson(someObjects)
//    }

    @TypeConverter
    fun categoryItemToString(category: List<CategoryEntity>?): String {
        return Gson().toJson(category)
    }

    @TypeConverter
    fun stringToCategoryItem(data: String?): List<CategoryEntity>? {
        val listType = object : TypeToken<List<CategoryEntity>>() {}.type
        return Gson().fromJson(data, listType)
    }
}