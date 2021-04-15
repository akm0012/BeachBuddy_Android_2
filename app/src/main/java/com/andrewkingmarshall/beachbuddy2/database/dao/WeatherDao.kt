package com.andrewkingmarshall.beachbuddy2.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andrewkingmarshall.beachbuddy2.database.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentUvInfo(currentUvInfo: CurrentUvInfo)

    // Note: Since this is not a list but a single item, this must be nullable as you will
    //  get null if there are no results that match
    @Query("SELECT * FROM CurrentUvInfo WHERE id = 'CurrentUvInfoPrimaryKey'")
    fun getCurrentUvInfo(): Flow<CurrentUvInfo?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(currentWeather: CurrentWeather)

    @Query("SELECT * FROM CurrentWeather WHERE id = 'CurrentWeatherPrimaryKey'")
    fun getCurrentWeather(): Flow<CurrentWeather?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyWeather(dailyWeatherInfo: DailyWeatherInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyWeatherList(dailyWeatherInfo: List<DailyWeatherInfo>)

    @Query("SELECT * FROM DailyWeatherInfo")
    fun getDailyWeather(): Flow<List<DailyWeatherInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyWeather(hourlyWeatherInfo: HourlyWeatherInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyWeatherList(hourlyWeatherInfo: List<HourlyWeatherInfo>)

    @Query("SELECT * FROM HourlyWeatherInfo")
    fun getHourlyWeather(): Flow<List<HourlyWeatherInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSunsetInfo(sunsetInfo: SunsetInfo)

    @Query("SELECT * FROM SunsetInfo WHERE id = 'SunsetInfoPrimaryKey'")
    fun getSunsetInfo(): Flow<SunsetInfo?>
}