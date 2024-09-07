package com.kathayat.railscarmaapplication.di

import android.content.Context
import androidx.room.Room
import com.kathayat.railscarmaapplication.repository.RoomRepository
import com.kathayat.railscarmaapplication.room.WifiDao
import com.kathayat.railscarmaapplication.room.WifiDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideWifiDao(wifiDatabase: WifiDatabase):WifiDao {
        return wifiDatabase.wifiDao()
    }
    @Provides
    fun provideRoomRepository(wifiDao: WifiDao): RoomRepository = RoomRepository(wifiDao)

    @Provides
    @Singleton
    fun provideWifiDatabase(@ApplicationContext context: Context): WifiDatabase {
        return Room.databaseBuilder(
            context = context,
            WifiDatabase::class.java,
            "wifidatabase"
        ) .fallbackToDestructiveMigration()
            .build()
    }
}