package com.stevehechio.milkyway.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
