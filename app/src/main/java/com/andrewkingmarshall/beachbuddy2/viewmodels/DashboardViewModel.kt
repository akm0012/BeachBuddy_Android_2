package com.andrewkingmarshall.beachbuddy2.viewmodels

import androidx.lifecycle.*
import com.andrewkingmarshall.beachbuddy2.database.model.*
import com.andrewkingmarshall.beachbuddy2.enums.FlagColor
import com.andrewkingmarshall.beachbuddy2.repository.DashboardRefreshError
import com.andrewkingmarshall.beachbuddy2.repository.DashboardRepository
import com.andrewkingmarshall.beachbuddy2.ui.domainmodels.WeatherDM
import com.andrewkingmarshall.beachbuddy2.ui.views.SunsetTimerView
import com.andrewkingmarshall.beachbuddy2.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import timber.log.Timber
import javax.inject.Inject

private const val AUTO_UPDATE_PRIME_TIME_COOLDOWN_MIN = 15
private const val AUTO_UPDATE_IDLE_COOLDOWN_MIN = 60
private const val AUTO_UPDATE_MILLIS = 5 * 60 * 1000L // 5 min

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
) : ViewModel() {

    private var lastDashboardRefresh = 0L

    val showToast: SingleLiveEvent<String> = SingleLiveEvent()
    val showLoadingEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()

    val dashboardRefreshErrorEvent = dashboardRepository.errorFlow.asLiveData()

    val usersWithScores: LiveData<List<UserWithScores>> =
        dashboardRepository.userWithScoresFlow
            .map { usersWithScores -> usersWithScores.sortedByDescending { it.user.totalScore } }
            .asLiveData()

    val weatherDomainModel: LiveData<WeatherDM?> =
        dashboardRepository.weatherDomainModelFlow.asLiveData()

    val isBeachClosed: LiveData<Boolean> =
        dashboardRepository.beachConditionsFlow.map { beachConditions ->
            beachConditions?.flagColor == FlagColor.DOUBLE_RED
        }.asLiveData()

    val beachConditions: LiveData<BeachConditions?> =
        dashboardRepository.beachConditionsFlow.asLiveData()

    val currentWeather: LiveData<CurrentWeather?> =
        dashboardRepository.currentWeatherFlow.asLiveData()

    val currentUvInfo: LiveData<CurrentUvInfo?> =
        dashboardRepository.currentUvInfoFlow.asLiveData()

    val hourlyWeatherInfo: LiveData<List<HourlyWeatherInfo>> =
        dashboardRepository.hourlyWeatherFlow.asLiveData()

    val dailyWeatherInfo: LiveData<List<DailyWeatherInfo>> =
        dashboardRepository.dailyWeatherFlow.asLiveData()

    val sunsetInfo: LiveData<SunsetInfo?> =
        dashboardRepository.sunsetInfoFlow.asLiveData()

    init {
        viewModelScope.launch {
            while (true) {
                onAutoUpdatePeriodHit()
                delay(AUTO_UPDATE_MILLIS)
            }
        }
    }

    /**
     * This is a simple timer that will emit "true" every X millis
     */
    val sunsetViewUpdateTimer = liveData {
        while (true) {
            emit(true)
            delay(SunsetTimerView.TIMER_DELAY)
        }
    }

    fun onSetSunscreenReminderForUser(user: User) {
        viewModelScope.launch {
            try {
                dashboardRepository.setSunScreenReminder(user)
                showToast.value = "Sunscreen Reminder Set"
            } catch (cause: Exception) {
                Timber.w(cause)
            }
        }
    }

    fun onPullToRefresh() {
        lastDashboardRefresh = System.currentTimeMillis()
        refreshDashboardData()
        viewModelScope.launch {
            try {
                dashboardRepository.refreshOtherDevices()
            } catch (cause: Exception) {
                Timber.w(cause, "Unable to refresh other devices.")
            }
        }
    }

    private fun refreshDashboardData() {
        viewModelScope.launch {
            try {
                dashboardRepository.refreshDashboard()
            } catch (error: DashboardRefreshError) {
                Timber.e(error, "Unable to refresh the dashboard! - ${error.localizedMessage}")
                showToast.value = error.message
            } finally {
                showLoadingEvent.value = false
            }
        }
    }

    private fun onAutoUpdatePeriodHit() {

        Timber.v("Auto Update Period hit.")

        val currentTime = System.currentTimeMillis()
        var shouldAutoUpdate = false

        if (lastDashboardRefresh == 0L) {
            Timber.d("We have not auto updated yet. Planning to update.")
            shouldAutoUpdate = true
        } else {

            val currentDateTime = DateTime(currentTime).withZone(DateTimeZone.getDefault())

            // If we are in "Prime Time" (07:00 - 22:00) update every 15 min
            if (currentDateTime.isAfter(DateTime().withHourOfDay(7).withMinuteOfHour(0)) &&
                currentDateTime.isBefore(DateTime().withHourOfDay(22).withMinuteOfHour(0))
            ) {
                Timber.d("We are in Prime Time!")
                // If it has been 15 min since last update.
                if (currentDateTime.isAfter(
                        DateTime(lastDashboardRefresh).plusMinutes(
                            AUTO_UPDATE_PRIME_TIME_COOLDOWN_MIN
                        )
                    )
                ) {
                    Timber.d("The Prime Time Cooldown of $AUTO_UPDATE_PRIME_TIME_COOLDOWN_MIN minutes is over. Planning to Update.")
                    shouldAutoUpdate = true
                } else {
                    Timber.d("The Prime Time Cooldown of $AUTO_UPDATE_PRIME_TIME_COOLDOWN_MIN minutes is still in effect. Not updating.")
                }

            } else {
                // Update every hour
                Timber.d("We are in idle Time")
                // If it has been an hour
                if (currentDateTime.isAfter(
                        DateTime(lastDashboardRefresh).plusMinutes(
                            AUTO_UPDATE_IDLE_COOLDOWN_MIN
                        )
                    )
                ) {
                    Timber.d("The Idle Time Cooldown of $AUTO_UPDATE_IDLE_COOLDOWN_MIN minutes is over. Planning to Update.")
                    shouldAutoUpdate = true
                } else {
                    Timber.d("The Idle Time Cooldown of $AUTO_UPDATE_IDLE_COOLDOWN_MIN minutes is still in effect. Not updating.")
                }
            }
        }

        if (shouldAutoUpdate) {
            Timber.i("Auto updating...")
            lastDashboardRefresh = currentTime
            refreshDashboardData()
        }
    }
}