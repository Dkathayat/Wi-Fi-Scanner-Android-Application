package com.kathayat.railscarmaapplication.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WifiData::class], version = 1)
abstract class WifiDatabase: RoomDatabase() {
    abstract fun wifiDao():WifiDao
}