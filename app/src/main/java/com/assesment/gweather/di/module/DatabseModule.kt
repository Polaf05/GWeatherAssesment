package com.assesment.gweather.di.module

import android.content.Context
import androidx.room.Room
import com.assesment.gweather.data.database.AppDatabase
import com.assesment.gweather.data.database.dao.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "weather_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideWeatherDao(db: AppDatabase): WeatherDao = db.weatherDao()
}