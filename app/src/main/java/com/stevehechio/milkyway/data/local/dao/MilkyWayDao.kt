package com.stevehechio.milkyway.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stevehechio.milkyway.data.local.entities.MilkyWayEntity
import io.reactivex.Observable

@Dao
interface MilkyWayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(milkyWayEntity: List<MilkyWayEntity>)

    @Query("DELETE FROM MILKY_WAY_TABLE")
    fun deleteAll()

    @Query("SELECT * FROM MILKY_WAY_TABLE")
    fun getMilkyWayImages(): Observable<List<MilkyWayEntity>>
}