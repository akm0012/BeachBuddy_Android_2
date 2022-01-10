package com.andrewkingmarshall.beachbuddy2.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.andrewkingmarshall.beachbuddy2.database.model.SunsetInfo
import com.andrewkingmarshall.beachbuddy2.enums.DarkModeType
import com.andrewkingmarshall.beachbuddy2.extensions.nextValue
import com.andrewkingmarshall.beachbuddy2.repository.DashboardRefreshError
import com.andrewkingmarshall.beachbuddy2.repository.DashboardRepository
import com.andrewkingmarshall.beachbuddy2.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import timber.log.Timber
import javax.inject.Inject

private const val AUTO_UPDATE_MILLIS = 5 * 60 * 1000L // 5 min

@HiltViewModel
class DarkModeViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
) : ViewModel() {

    private var darkModeType = DarkModeType.AUTO
    val isDarkModeActive: MutableLiveData<Boolean> = MutableLiveData(false)

    val showToast: SingleLiveEvent<String> = SingleLiveEvent()

    private val sunsetInfoFlow = dashboardRepository.sunsetInfoFlow

    init {
        viewModelScope.launch {
            while (true) {
                checkForDarkModeUpdate(sunsetInfoFlow.first())
                delay(AUTO_UPDATE_MILLIS)
            }
        }
    }

    /**
     * This will cycle through the different Dark Mode settings.
     */
    fun onDarkModeButtonPressed() {
        darkModeType = darkModeType.nextValue()
        showToast.value = darkModeType.toString()

        when (darkModeType) {
            DarkModeType.AUTO ->
                viewModelScope.launch {
                    checkForDarkModeUpdate(sunsetInfoFlow.first())
                }
            DarkModeType.ON -> isDarkModeActive.value = true
            DarkModeType.OFF -> isDarkModeActive.value = false
        }
    }

    @SuppressLint("BinaryOperationInTimber")
    private fun checkForDarkModeUpdate(sunsetInfo: SunsetInfo?) {

        // Things will only change if Dark Mode is set to Auto
        if (darkModeType == DarkModeType.AUTO && sunsetInfo != null) {
            val currentTime = System.currentTimeMillis()

            Timber.d("Checking for Dark Mode Update. ")
            Timber.d("Current Time: ${DateTime(currentTime).toString("yyyy-MM-dd'T'HH:mm:ssZ")} ($currentTime)")
            Timber.d("Sunrise Time: ${DateTime(sunsetInfo.sunrise).toString("yyyy-MM-dd'T'HH:mm:ssZ")} (${sunsetInfo.sunrise})")
            Timber.d("Sunset Time: ${DateTime(sunsetInfo.sunset).toString("yyyy-MM-dd'T'HH:mm:ssZ")} (${sunsetInfo.sunset})")

            if (currentTime < sunsetInfo.sunset && currentTime > sunsetInfo.sunrise) {
                // It is light outside
                Timber.d("It's light outside. Dark mode is not active.")
                isDarkModeActive.value = false
            } else {
                Timber.d("It's dark outside. Dark mode is active.")
                isDarkModeActive.value = true
            }
        }
    }
}