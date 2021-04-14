package com.andrewkingmarshall.beachbuddy2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.andrewkingmarshall.beachbuddy2.repository.FirebaseRepository
import com.andrewkingmarshall.beachbuddy2.repository.RequestedItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val requestedItemRepository: RequestedItemRepository,
) : ViewModel() {

    val notCompletedItemCount = requestedItemRepository.getRequestedItemsDomainModel()
        .map { it.nonCompletedItems.count() }
        .asLiveData()

    init {
        viewModelScope.launch {
            firebaseRepository.registerDevice()
        }
    }


}