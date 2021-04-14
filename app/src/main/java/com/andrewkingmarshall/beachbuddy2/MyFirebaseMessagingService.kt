package com.andrewkingmarshall.beachbuddy2

import android.media.MediaPlayer
import com.andrewkingmarshall.beachbuddy2.repository.FirebaseRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var firebaseRepository: FirebaseRepository

//    @Inject
//    lateinit var requestedItemRepository: RequestedItemRepository

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Timber.d("From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            Timber.d("Message data payload: ${remoteMessage.data}")

            // Refresh the Requested Item data
            handleNow()

            remoteMessage.data["updateOnly"]?.let {
                Timber.d("updateOnly: $it")

                // Play a Seagull noise
                if (it == "false") {
                    // Play the Seagull sound
                    val mPlayer: MediaPlayer = MediaPlayer.create(applicationContext, R.raw.seagulls)
                    mPlayer.start()
                }
            }

            remoteMessage.data["name"]?.let {
                Timber.d("name: $it")
            }

            remoteMessage.data["count"]?.let {
                Timber.d("count: $it")
            }

            remoteMessage.data["sentByUserId"]?.let {
                Timber.d("sentByUserId: $it")
            }
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Timber.d("Refreshed token: $token")

        serviceScope.launch {
            firebaseRepository.registerFCMToken(token)
        }
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
//        Timber.d("Refreshing Requested Items.")
//
//        requestedItemRepository.refreshRequestedItems()
    }

}