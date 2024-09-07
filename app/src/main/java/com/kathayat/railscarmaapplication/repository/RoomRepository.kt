package com.kathayat.railscarmaapplication.repository

import android.util.Log
import com.kathayat.railscarmaapplication.room.WifiDao
import com.kathayat.railscarmaapplication.room.WifiData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val wifiDao: WifiDao
) {
    val getAllWifi: Flow<List<WifiData>> = wifiDao.getAllWifi()

    suspend fun insert(wifiData: WifiData){
        try {
            wifiDao.insertWifi(wifiData)
        } catch (e: Exception) {
            Log.e("RoomViewModel", "Error inserting WifiData", e)
        }
    }

    suspend fun update(wifiData: WifiData){
        wifiDao.updateWifi(wifiData)
    }

    suspend fun delete(wifiData: WifiData){
        wifiDao.deleteWifi(wifiData)
    }

}