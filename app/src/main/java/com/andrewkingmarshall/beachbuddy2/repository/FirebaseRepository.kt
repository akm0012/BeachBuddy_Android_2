package com.andrewkingmarshall.beachbuddy2.repository

import com.andrewkingmarshall.beachbuddy2.network.requests.AddDeviceRequest
import com.andrewkingmarshall.beachbuddy2.network.service.ApiService
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor(
    private val apiService: ApiService,
) {

    suspend fun registerDevice() {
        Timber.d("Registering Device...")

        try {
            val token = FirebaseMessaging.getInstance().token.await()
            Timber.d("Token from register device: $token")
            registerFCMToken(token)
        } catch (e: Exception) {
            Timber.w(e, "Fetching FCM registration token failed")
        }
    }

    suspend fun registerFCMToken(fcmToken: String) {

        Timber.d("Updating FCM Token: $fcmToken")

        try {
            apiService.addDevice(AddDeviceRequest(fcmToken))
            Timber.d("Fcm Token was sent up successfully.")
        } catch (cause: Throwable) {
            Timber.w(
                cause,
                "Something went wrong when updating fcm token: ${cause.localizedMessage}"
            )
        }
    }

}