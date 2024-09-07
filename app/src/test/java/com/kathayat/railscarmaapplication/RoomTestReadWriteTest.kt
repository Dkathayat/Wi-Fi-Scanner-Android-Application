package com.kathayat.railscarmaapplication

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kathayat.railscarmaapplication.room.WifiDao
import com.kathayat.railscarmaapplication.room.WifiData
import com.kathayat.railscarmaapplication.room.WifiDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomTestReadWriteTest {
    private lateinit var wifiDao: WifiDao
    private lateinit var db: WifiDatabase
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, WifiDatabase::class.java).build()
        wifiDao = db.wifiDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
     fun writeUserAndReadInList() = runBlocking {
        val user = WifiData(1,"Deepak")
        wifiDao.insertWifi(user)
        val result = wifiDao.getAllWifi()
        Log.d("DatabaseTest",result.toString())
    }
}