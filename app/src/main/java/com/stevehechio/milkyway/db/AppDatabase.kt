package com.stevehechio.milkyway.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stevehechio.milkyway.data.local.convertors.LinkConverter
import com.stevehechio.milkyway.data.local.convertors.MilkyWayTypeConvertor
import com.stevehechio.milkyway.data.local.dao.MilkyWayDao
import com.stevehechio.milkyway.data.local.dao.RemoteKeysDao
import com.stevehechio.milkyway.data.local.entities.MilkyWayEntity
import com.stevehechio.milkyway.data.local.entities.RemoteKeys
import com.stevehechio.milkyway.utils.AppConstants


@Database(entities = [MilkyWayEntity::class, RemoteKeys::class],
    version = AppConstants.DB_VERSION, exportSchema = false)
@TypeConverters(MilkyWayTypeConvertor::class, LinkConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun milkyWayDao(): MilkyWayDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    //Room should only be initiated once, marked volatile to be thread safe.

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK){
                instance ?: buildDatabase(context)
                    .also { instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                AppConstants.DB_NAME
            ).fallbackToDestructiveMigration()
                .build()
    }
}