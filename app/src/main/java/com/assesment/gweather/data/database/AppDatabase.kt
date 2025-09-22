package com.assesment.gweather.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.assesment.gweather.data.database.dao.WeatherDao
import com.assesment.gweather.data.database.entity.WeatherEntity

@Database(entities = [WeatherEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
