package com.kathayat.railscarmaapplication.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WifiDao {
    @Query("SELECT * FROM wifidatabase")
    fun getAllWifi(): Flow<List<WifiData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWifi(wifiData: WifiData)

    @Update
    suspend fun updateWifi(wifiData: WifiData)

    @Delete
    suspend fun deleteWifi(wifiData: WifiData)
}