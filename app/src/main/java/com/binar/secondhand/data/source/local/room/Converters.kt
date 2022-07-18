package com.binar.secondhand.data.source.local.room

import androidx.room.TypeConverter
import com.binar.secondhand.data.source.local.entity.CategoryEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
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