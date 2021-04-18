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

    val titleString = MutableLiveData<String>()
    val itemsAddedString = MutableLiveData<String>()

    private var isDialogVisible = false

    init {
        viewModelScope.launch {
            itemRepository.newItemsFlow.collect {

                if (isDialogVisible) {
                    itemsAddedString.value = "${it.getNameAndQuantity()}\n${itemsAddedString.value}"
                    setTitle(it, true)
                    restartTimerEvent.call()
                } else {
                    itemsAddedString.value = it.getNameAndQuantity()
                    setTitle(it)
                    showNewItemAddedDialogEvent.call()
                }
            }
        }
    }

    private fun setTitle(it: RequestedItem, forcePlural:Boolean = false) {
        if (it.count > 1 || forcePlural) {
            titleString.value = "Items Added to the Beach List!"
        } else {
            titleString.value = "Item Added to the Beach List!"
        }
    }

    fun onDialogShown() {
        isDialogVisible = true
    }

    fun onDialogClosed() {
        isDialogVisible = false
    }

}