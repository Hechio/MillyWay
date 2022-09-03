package com.stevehechio.milkyway.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.stevehechio.milkyway.local.convertors.LinkConverter
import com.stevehechio.milkyway.local.convertors.MilkyWayTypeConvertor
import com.stevehechio.milkyway.utils.AppConstants
import java.io.Serializable

@Entity(tableName = AppConstants.TABLE_NAME)
data class MilkyWayEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @TypeConverters(MilkyWayTypeConvertor::class)
    @field:SerializedName("data") val milkyWayData: List<MilkyWayData>,
    @TypeConverters(LinkConverter::class)
    val links: List<Link>
): Serializable

data class MilkyWayData(
    val center: String,
    val date_created: String,
    val description: String,
    val nasa_id: String,
    val title: String
): Serializable

data class Link(
    @field:SerializedName("href") val imageUrl: String
): Serializable