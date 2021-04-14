package com.andrewkingmarshall.beachbuddy2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrewkingmarshall.beachbuddy2.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
) : ViewModel() {

    init {
        viewModelScope.launch {
            firebaseRepository.registerDevice()
        }
    }


}