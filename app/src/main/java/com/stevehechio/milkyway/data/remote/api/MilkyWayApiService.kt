package com.stevehechio.milkyway.data.remote.api

import com.stevehechio.milkyway.data.remote.model.MilkyWayApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MilkyWayApiService {
    //https://images-api.nasa.gov/search?q=milky%20way&media_type=image&year_start=2017&year_end=2017

    @GET("search?q=milky%20way&media_type=image&year_start=2017&year_end=2017")
    suspend fun fetchMilkyWayImages(): MilkyWayApiResponse
}