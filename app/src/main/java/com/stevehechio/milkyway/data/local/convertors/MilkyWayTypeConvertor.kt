package com.stevehechio.milkyway.data.local.convertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.stevehechio.milkyway.data.local.entities.Link
import com.stevehechio.milkyway.data.local.entities.MilkyWayData

object MilkyWayTypeConvertor {
    @TypeConverter
    fun fromStringToMilkyWayList(value: String): List<MilkyWayData>? {
        val listType = object : TypeToken<List<MilkyWayData>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromMilkyWayListToString(list: List<MilkyWayData>?): String?{
        val gson = Gson()
        return gson.toJson(list)
    }
}

object LinkConverter {
    @TypeConverter
    fun fromStringToLinkList(value: String): List<Link>? {
        val listType = object : TypeToken<List<Link>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromLinkListToString(list: List<Link>?): String?{
        val gson = Gson()
        return gson.toJson(list)
    }
}