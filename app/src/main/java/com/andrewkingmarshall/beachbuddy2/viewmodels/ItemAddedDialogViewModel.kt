package com.andrewkingmarshall.beachbuddy2.viewmodels

import androidx.lifecycle.*
import com.andrewkingmarshall.beachbuddy2.database.model.RequestedItem
import com.andrewkingmarshall.beachbuddy2.repository.RequestedItemRepository
import com.andrewkingmarshall.beachbuddy2.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ItemAddedDialogViewModel @Inject constructor(
    private val itemRepository: RequestedItemRepository,
) : ViewModel() {

    val showNewItemAddedDialogEvent = SingleLiveEvent<RequestedItem>()

    val restartTimerEvent = SingleLiveEvent<RequestedItem>()

    private var isDialogVisible = false

    init {
        viewModelScope.launch {
            itemRepository.newItemsFlow.collect {

                if (isDialogVisible) {
                    restartTimerEvent.call()
                } else {
                    showNewItemAddedDialogEvent.call()
                }
            }
        }
    }

    fun onDialogShown() {
        isDialogVisible = true
    }

    fun onDialogClosed() {
        isDialogVisible = false
    }

}