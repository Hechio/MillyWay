package com.stevehechio.milkyway.data.local.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.stevehechio.milkyway.data.local.dao.MilkyWayDao
import com.stevehechio.milkyway.data.local.entities.Link
import com.stevehechio.milkyway.data.local.entities.MilkyWayData
import com.stevehechio.milkyway.data.local.entities.MilkyWayEntity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class AppDatabaseTest {
    private  lateinit var database: AppDatabase
    private lateinit var milkyWayDao: MilkyWayDao

    @Before
   fun setUp() {
       database = Room.inMemoryDatabaseBuilder(
           ApplicationProvider.getApplicationContext(),
           AppDatabase::class.java)
           .allowMainThreadQueries()
           .build()
       milkyWayDao = database.milkyWayDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndGetMilkyImages(){
        val entities = arrayListOf(
            MilkyWayEntity(1,
            arrayListOf(MilkyWayData("center","date_created",
                "description","nasa_id","Hechio Testing")),
                arrayListOf(Link("stevehechio.com"))))
        milkyWayDao.insertAll(entities)

        val resEntity = milkyWayDao.getMilkyWayImages().blockingFirst()
        assertEquals("stevehechio.com",resEntity.first().links.first().imageUrl)
    }
}