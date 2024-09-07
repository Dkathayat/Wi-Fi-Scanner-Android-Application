package com.kathayat.railscarmaapplication.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "wifidatabase",
    indices = [Index(value = ["Wifi Name"], unique = true)])
data class WifiData(
    @ColumnInfo(name="Wifi Name") var name: String,
    @ColumnInfo(name="Signal Strength") var strenght: Int,
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
){
    companion object {
        fun createWifiData(name: String, strength: Int): WifiData {
            return WifiData(name = name, strenght = strength)
        }
    }
}
