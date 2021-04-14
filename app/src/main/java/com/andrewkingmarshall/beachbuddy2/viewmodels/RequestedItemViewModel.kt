package com.andrewkingmarshall.beachbuddy2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.andrewkingmarshall.beachbuddy2.database.model.RequestedItem
import com.andrewkingmarshall.beachbuddy2.repository.RequestedItemRepository
import com.andrewkingmarshall.beachbuddy2.ui.domainmodels.RequestedItemsDM
import com.andrewkingmarshall.beachbuddy2.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestedItemViewModel @Inject constructor(
    private val requestedItemRepository: RequestedItemRepository,
) : ViewModel() {

    val showToast: SingleLiveEvent<String> = SingleLiveEvent()
    val showLoadingEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()

    val requestedItemsDomainModel: LiveData<RequestedItemsDM> =
        requestedItemRepository.getRequestedItemsDomainModel().asLiveData()

    fun onRequestedItemChecked(requestedItem: RequestedItem) {
        viewModelScope.launch {
            try {
                requestedItemRepository.markRequestedItemAsComplete(requestedItem)
            } catch (e: Exception) {
                showToast.value = e.message
            }
        }
    }

    fun onPullToRefresh() {
        viewModelScope.launch {
            try {
                requestedItemRepository.refreshRequestedItems()
            } catch (e: Exception) {
                showToast.value = e.message
            } finally {
                showLoadingEvent.value = false
            }
        }
    }
}