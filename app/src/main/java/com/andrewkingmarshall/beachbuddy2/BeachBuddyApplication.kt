package com.andrewkingmarshall.beachbuddy2

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import net.danlew.android.joda.JodaTimeAndroid
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BeachBuddyApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        JodaTimeAndroid.init(this)

        setUpLogging()

        setUpPushChannels()

        setUpWorkerManager()
    }

    private fun setUpWorkerManager() {

    }

    private fun setUpLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setUpPushChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "Requested Items"
            val descriptionText = "Be notified when new Requested Items come in."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("RequestedItemsChannel", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}