package com.andrewkingmarshall.beachbuddy2.repository

import com.andrewkingmarshall.beachbuddy2.database.dao.BeachConditionsDao
import com.andrewkingmarshall.beachbuddy2.database.dao.UserDao
import com.andrewkingmarshall.beachbuddy2.database.dao.WeatherDao
import com.andrewkingmarshall.beachbuddy2.database.model.*
import com.andrewkingmarshall.beachbuddy2.network.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

const val SRQ_LAT = 27.267804
const val SRQ_LON = -82.553679

const val NumOfHoursToSave = 24
const val NumOfDaysToSave = 8

@Singleton
class DashboardRepository @Inject constructor(
    private val apiService: ApiService,
    private val beachConditionsDao: BeachConditionsDao,
    private val userDao: UserDao,
    private val weatherDao: WeatherDao,
) {

    // Todo: Make it so all these chunks process at once
    // Todo: Add a Flow of Errors (maybe channel) that I can emit to and listen for in UI (if listening)
    suspend fun refreshDashboard() {
        Timber.i("Starting to refresh dashboard data...")

        withContext(Dispatchers.IO) {

            try {

                Timber.v("Starting API Call to get Dashboard Data...")
                val dashboardDto = apiService.getDashboard(lat = SRQ_LAT, lon = SRQ_LON)
                Timber.v("Done - API Call to get Dashboard Data.")

                // Users
                Timber.v("Starting to process Users...")
                val usersToSave = ArrayList<User>()
                dashboardDto.users.forEach { userDto ->
                    try {
                        usersToSave.add(User(userDto))
                    } catch (e: Exception) {
                        Timber.w(e, "Unable to process item. Skipping it. $userDto")
                    }
                }
                Timber.v("Done processing Users.")
                Timber.d("Saving Users...")
                userDao.insertUsers(usersToSave)
                Timber.d("Done saving Users.")

                // Current Weather Info
                Timber.v("Starting to process Current Weather Info...")
                try {
                    val currentWeather = CurrentWeather(dashboardDto.weatherDto)
                    Timber.v("Done processing Current Weather Info.")
                    Timber.d("Saving Current Weather...")
                    weatherDao.insertCurrentWeather(currentWeather)
                    Timber.d("Done saving Current Weather.")
                } catch (e: Exception) {
                    Timber.w(
                        e,
                        "Unable to process CurrentWeather. Skipping it. ${dashboardDto.weatherDto}"
                    )
                }

                // Beach Conditions
                Timber.v("Starting to process Beach Conditions...")
                try {
                    val beachConditions = BeachConditions(dashboardDto.beachConditions)
                    Timber.v("Done processing Beach Conditions.")
                    Timber.d("Saving Beach Conditions...")
                    beachConditionsDao.insertBeachConditions(beachConditions)
                    Timber.d("Done saving Beach Conditions.")
                } catch (e: Exception) {
                    Timber.w(
                        e,
                        "Unable to process Beach Conditions. Skipping it. ${dashboardDto.beachConditions}"
                    )
                }

                // UV Info
                Timber.v("Starting to process UV Info...")
                try {
                    val uvInfo = CurrentUvInfo(dashboardDto.currentUvDto)
                    Timber.v("Done processing UV Info.")
                    Timber.d("Saving Current UV Info...")
                    weatherDao.insertCurrentUvInfo(uvInfo)
                    Timber.d("Done saving Current UV Info.")
                } catch (e: Exception) {
                    Timber.w(
                        e,
                        "Unable to process UV Info. Skipping it. ${dashboardDto.currentUvDto}"
                    )
                }

                // Hourly Weather
                Timber.v("Starting to process Hourly Weather...")
                val hourlyInfoToSave = ArrayList<HourlyWeatherInfo>()
                repeat(NumOfHoursToSave) {
                    try {
                        val hourlyWeatherInfo =
                            HourlyWeatherInfo(it, dashboardDto.weatherDto.hourly[it])
                        hourlyInfoToSave.add(hourlyWeatherInfo)
                    } catch (e: Exception) {
                        Timber.w(e, "Unable to process Hourly Weather. Skipping Index $it")
                    }
                }
                Timber.v("Done processing Hourly Weather.")
                Timber.d("Saving Hourly Weather...")
                weatherDao.insertHourlyWeatherList(hourlyInfoToSave)
                Timber.d("Done saving Hourly Weather.")

                // Daily Weather
                Timber.v("Starting to process Daily Weather...")
                val dailyInfoToSave = ArrayList<DailyWeatherInfo>()
                repeat(NumOfDaysToSave) {
                    try {
                        val dailyWeatherInfo =
                            DailyWeatherInfo(it, dashboardDto.weatherDto.daily[it])
                        dailyInfoToSave.add(dailyWeatherInfo)
                    } catch (e: Exception) {
                        Timber.w(e, "Unable to process Daily Weather. Skipping Index $it")
                    }
                }
                Timber.v("Done processing Daily Weather.")
                Timber.d("Saving Daily Weather...")
                weatherDao.insertDailyWeatherList(dailyInfoToSave)
                Timber.d("Done saving Daily Weather.")

                // Sunset Info
                Timber.v("Starting to process Sunset Info...")
                try {
                    val sunsetInfo = SunsetInfo(dashboardDto.weatherDto)
                    Timber.v("Done processing Sunset Info.")
                    Timber.d("Saving Sunset Info...")
                    weatherDao.insertSunsetInfo(sunsetInfo)
                    Timber.d("Done saving Sunset Info.")
                } catch (e: Exception) {
                    Timber.w(
                        e,
                        "Unable to process SunsetInfo. Skipping it. ${dashboardDto.weatherDto}"
                    )
                }

            } catch (cause: Exception) {
                throw DashboardRefreshError(
                    cause.localizedMessage ?: "Unable to update the Dashboard",
                    cause
                )
            }
        }

        Timber.i("Done refreshing dashboard data.")
    }

}

class DashboardRefreshError(message: String, cause: Throwable?) : Throwable(message, cause)