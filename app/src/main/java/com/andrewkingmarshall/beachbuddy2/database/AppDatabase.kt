package com.andrewkingmarshall.beachbuddy2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andrewkingmarshall.beachbuddy2.database.dao.BeachConditionsDao
import com.andrewkingmarshall.beachbuddy2.database.dao.UserDao
import com.andrewkingmarshall.beachbuddy2.database.dao.WeatherDao
import com.andrewkingmarshall.beachbuddy2.database.model.*

@Database(
    entities = [
        BeachConditions::class,
        CurrentUvInfo::class,
        CurrentWeather::class,
        DailyWeatherInfo::class,
        HourlyWeatherInfo::class,
        RequestedItem::class,
        Score::class,
        SunsetInfo::class,
        User::class,
        UserScoreCrossRef::class,
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun beachConditionsDao(): BeachConditionsDao
    abstract fun userDao(): UserDao
    abstract fun weatherDao(): WeatherDao
}