package com.stevehechio.milkyway.data.api

import com.stevehechio.milkyway.data.remote.api.MilkyWayApiService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*

@RunWith(JUnit4::class)
open class MilkyWayApiServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var milkyWayApiService: MilkyWayApiService

    @Before
    @Throws(IOException::class)
    fun openServer(){
        mockWebServer = MockWebServer()
        mockWebServer.start()
        milkyWayApiService = createService(MilkyWayApiService::class.java)
    }

    private fun createService(clazz: Class<MilkyWayApiService>): MilkyWayApiService {
        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(clazz)
    }
    @After
    @Throws(IOException::class)
    fun closeServer(){
        mockWebServer.shutdown()
    }

    @Test
    fun testFetchingMilkWayImages(){
        val inputStream = Objects.requireNonNull(
            MilkyWayApiService::class.java.classLoader
        ).getResourceAsStream(String.format("api-response/%s", "milky.json"))

        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockWebServer.enqueue(mockResponse.setBody(source.readString(StandardCharsets.UTF_8)))
        val entity = milkyWayApiService.fetchMilkyWayImages().blockingFirst().result.items
        Assert.assertEquals("GSFC_20171208_Archive_e000261",
            entity.first().milkyWayData.first().nasa_id)
    }
}