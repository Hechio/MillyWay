package com.stevehechio.milkyway.data.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.stevehechio.milkyway.data.local.entities.MilkyWayEntity
import java.io.Serializable

data class MilkyWayApiResponse(
    @SerializedName("collection")
    @Expose
    val result: Result
): Serializable
data class Result(
    val items: List<MilkyWayEntity>,
): Serializable