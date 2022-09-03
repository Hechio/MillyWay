package com.stevehechio.milkyway.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stevehechio.milkyway.local.entities.MilkyWayEntity
import io.reactivex.Observable

@Dao
interface MilkyWayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(milkyWayEntity: List<MilkyWayEntity>)

    @Query("DELETE FROM MILKY_WAY_TABLE")
    suspend fun deleteAll()

    /**Instead of returning a List<CardsEntity>, return PagingSource<Int, CardsEntity>.
    That way, the cads table becomes the source of data for Paging. */

    @Query("SELECT * FROM MILKY_WAY_TABLE")
    fun getAllMilkyWayImages(): PagingSource<Int, MilkyWayEntity>

    @Query("SELECT * FROM MILKY_WAY_TABLE")
    fun getMilkyWayImages(): Observable<List<MilkyWayEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForTest(milkyWayEntity: List<MilkyWayEntity>)
}