package com.andrewkingmarshall.beachbuddy2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.andrewkingmarshall.beachbuddy2.database.model.*
import com.andrewkingmarshall.beachbuddy2.repository.DashboardRepository
import com.andrewkingmarshall.beachbuddy2.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
) : ViewModel() {

    val showToast: SingleLiveEvent<String> = SingleLiveEvent()
    val showLoadingEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()

    val usersWithScores: LiveData<List<UserWithScores>> =
        dashboardRepository.userWithScoresFlow.asLiveData()

    val beachConditions: LiveData<BeachConditions> =
        dashboardRepository.beachConditionsFlow.asLiveData()

    val currentWeather: LiveData<CurrentWeather> =
        dashboardRepository.currentWeatherFlow.asLiveData()

    val currentUvInfo: LiveData<CurrentUvInfo> =
        dashboardRepository.currentUvInfoFlow.asLiveData()

    val hourlyWeatherInfo: LiveData<List<HourlyWeatherInfo>> =
        dashboardRepository.hourlyWeatherFlow.asLiveData()

    val dailyWeatherInfo: LiveData<List<DailyWeatherInfo>> =
        dashboardRepository.dailyWeatherFlow.asLiveData()

    val sunsetInfo: LiveData<SunsetInfo> =
        dashboardRepository.sunsetInfoFlow.asLiveData()

    fun refreshDashboard() {
        viewModelScope.launch {
            dashboardRepository.refreshDashboard()
        }
    }

}