package com.andrewkingmarshall.beachbuddy2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrewkingmarshall.beachbuddy2.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
) : ViewModel() {

    fun refreshDashboard() {
        viewModelScope.launch {
            dashboardRepository.refreshDashboard()
        }
    }

}