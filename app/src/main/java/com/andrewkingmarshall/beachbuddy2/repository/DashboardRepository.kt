package com.andrewkingmarshall.beachbuddy2.repository

import com.andrewkingmarshall.beachbuddy2.network.dtos.DashboardDto
import com.andrewkingmarshall.beachbuddy2.database.dao.BeachConditionsDao
import com.andrewkingmarshall.beachbuddy2.database.dao.UserDao
import com.andrewkingmarshall.beachbuddy2.database.dao.WeatherDao
import com.andrewkingmarshall.beachbuddy2.database.model.*
import com.andrewkingmarshall.beachbuddy2.network.service.ApiService
import kotlinx.coroutines.*
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

    val userWithScoresFlow = userDao.getUsersWithScores()
    val beachConditionsFlow = beachConditionsDao.getBeachConditions()
    val currentWeatherFlow = weatherDao.getCurrentWeather()
    val currentUvInfoFlow = weatherDao.getCurrentUvInfo()
    val hourlyWeatherFlow = weatherDao.getHourlyWeather()
    val dailyWeatherFlow = weatherDao.getDailyWeather()
    val sunsetInfoFlow = weatherDao.getSunsetInfo()

    /**
     * This will make a call to get the Dashboard data, and then async process all the data that is returned.
     */
    // Todo: Add a Flow of Errors (maybe channel) that I can emit to and listen for in UI (if listening)
    suspend fun refreshDashboard() {
        Timber.i("Starting to refresh dashboard data...")

        withContext(Dispatchers.IO) {

            try {
                Timber.v("Starting API Call to get Dashboard Data...")
                val dashboardDto = apiService.getDashboard(lat = SRQ_LAT, lon = SRQ_LON)
                Timber.v("Done - API Call to get Dashboard Data.")

                val deferredWork = ArrayList<Deferred<Unit>>()

                deferredWork.add(async { processUsers(dashboardDto) })
                deferredWork.add(async { processUserScores(dashboardDto) })
                deferredWork.add(async { processCurrentWeatherInfo(dashboardDto) })
                deferredWork.add(async { processBeachConditions(dashboardDto) })
                deferredWork.add(async { processUvInfo(dashboardDto) })
                deferredWork.add(async { processHourlyWeather(dashboardDto) })
                deferredWork.add(async { processDailyWeather(dashboardDto) })
                deferredWork.add(async { processSunsetInfo(dashboardDto) })

                // Process everything at once
                // Note: The work above will have already started by this line,
                //  however it will wait here until it's all done.
                Timber.d("Await all process work...")
                deferredWork.awaitAll()
                Timber.d("Done - Await all process work.")

            } catch (cause: Throwable) {
                throw DashboardRefreshError(
                    cause.localizedMessage ?: "Unable to update the Dashboard",
                    cause
                )
            }
        }

        Timber.i("Done refreshing dashboard data.")
    }

    private suspend fun processSunsetInfo(dashboardDto: DashboardDto) {
        // Sunset Info
        Timber.v("Starting to process Sunset Info...")
        try {
            val sunsetInfo = SunsetInfo(dashboardDto.weatherDto)
            Timber.v("Done processing Sunset Info.")
            Timber.d("Saving Sunset Info...")
            weatherDao.insertSunsetInfo(sunsetInfo)
            Timber.d("Done saving Sunset Info.")
        } catch (e: Throwable) {
            Timber.w(
                e,
                "Unable to process SunsetInfo. Skipping it. ${dashboardDto.weatherDto}"
            )
        }
    }

    private suspend fun processDailyWeather(dashboardDto: DashboardDto) {
        // Daily Weather
        Timber.v("Starting to process Daily Weather...")
        val dailyInfoToSave = ArrayList<DailyWeatherInfo>()
        repeat(NumOfDaysToSave) {
            try {
                val dailyWeatherInfo =
                    DailyWeatherInfo(it, dashboardDto.weatherDto.daily[it])
                dailyInfoToSave.add(dailyWeatherInfo)
            } catch (e: Throwable) {
                Timber.w(e, "Unable to process Daily Weather. Skipping Index $it")
            }
        }
        Timber.v("Done processing Daily Weather.")
        Timber.d("Saving Daily Weather...")
        weatherDao.insertDailyWeatherList(dailyInfoToSave)
        Timber.d("Done saving Daily Weather.")
    }

    private suspend fun processHourlyWeather(dashboardDto: DashboardDto) {
        // Hourly Weather
        Timber.v("Starting to process Hourly Weather...")
        val hourlyInfoToSave = ArrayList<HourlyWeatherInfo>()
        repeat(NumOfHoursToSave) {
            try {
                val hourlyWeatherInfo =
                    HourlyWeatherInfo(it, dashboardDto.weatherDto.hourly[it])
                hourlyInfoToSave.add(hourlyWeatherInfo)
            } catch (e: Throwable) {
                Timber.w(e, "Unable to process Hourly Weather. Skipping Index $it")
            }
        }
        Timber.v("Done processing Hourly Weather.")
        Timber.d("Saving Hourly Weather...")
        weatherDao.insertHourlyWeatherList(hourlyInfoToSave)
        Timber.d("Done saving Hourly Weather.")
    }

    private suspend fun processUvInfo(dashboardDto: DashboardDto) {
        // UV Info
        Timber.v("Starting to process UV Info...")
        try {
            val uvInfo = CurrentUvInfo(dashboardDto.currentUvDto)
            Timber.v("Done processing UV Info.")
            Timber.d("Saving Current UV Info...")
            weatherDao.insertCurrentUvInfo(uvInfo)
            Timber.d("Done saving Current UV Info.")
        } catch (e: Throwable) {
            Timber.w(
                e,
                "Unable to process UV Info. Skipping it. ${dashboardDto.currentUvDto}"
            )
        }
    }

    private suspend fun processBeachConditions(dashboardDto: DashboardDto) {
        // Beach Conditions
        Timber.v("Starting to process Beach Conditions...")
        try {
            val beachConditions = BeachConditions(dashboardDto.beachConditions)
            Timber.v("Done processing Beach Conditions.")
            Timber.d("Saving Beach Conditions...")
            beachConditionsDao.insertBeachConditions(beachConditions)
            Timber.d("Done saving Beach Conditions.")
        } catch (e: Throwable) {
            Timber.w(
                e,
                "Unable to process Beach Conditions. Skipping it. ${dashboardDto.beachConditions}"
            )
        }
    }

    private suspend fun processCurrentWeatherInfo(dashboardDto: DashboardDto) {
        // Current Weather Info
        Timber.v("Starting to process Current Weather Info...")
        try {
            val currentWeather = CurrentWeather(dashboardDto.weatherDto)
            Timber.v("Done processing Current Weather Info.")
            Timber.d("Saving Current Weather...")
            weatherDao.insertCurrentWeather(currentWeather)
            Timber.d("Done saving Current Weather.")
        } catch (e: Throwable) {
            Timber.w(
                e,
                "Unable to process CurrentWeather. Skipping it. ${dashboardDto.weatherDto}"
            )
        }
    }

    private suspend fun processUserScores(dashboardDto: DashboardDto) {
        // Users
        Timber.v("Starting to process Scores...")
        val scoresToSave = ArrayList<Score>()
        dashboardDto.users.forEach { userDto ->
            userDto.scores.forEach { scoreDto ->
                try {
                    scoresToSave.add(Score(scoreDto))
                } catch (e: Throwable) {
                    Timber.w(
                        e,
                        "Unable to process item. Skipping it. UserDto: $userDto and ScoreDto: $scoreDto"
                    )
                }
            }
        }
        Timber.v("Done processing Scores.")
        Timber.d("Saving Scores...")
        userDao.insertScores(scoresToSave)
        Timber.d("Done saving Scores.")
    }

    private suspend fun processUsers(dashboardDto: DashboardDto) {
        // Users
        Timber.v("Starting to process Users...")
        val usersToSave = ArrayList<User>()
        dashboardDto.users.forEach { userDto ->
            try {
                usersToSave.add(User(userDto))
            } catch (e: Throwable) {
                Timber.w(e, "Unable to process item. Skipping it. $userDto")
            }
        }
        Timber.v("Done processing Users.")
        Timber.d("Saving Users...")
        userDao.insertUsers(usersToSave)
        Timber.d("Done saving Users.")
    }

}

class DashboardRefreshError(message: String, cause: Throwable?) : Throwable(message, cause)