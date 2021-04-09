package com.andrewkingmarshall.beachbuddy2.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.andrewkingmarshall.beachbuddy2.R
import com.andrewkingmarshall.beachbuddy2.database.AppDatabase
import com.andrewkingmarshall.beachbuddy2.database.model.BeachConditions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            db.beachConditionsDao().getBeachConditions().collect {
                Timber.d("$it")
            }
        }

        saveRandomBeachConditions.setOnClickListener {

            lifecycleScope.launch {
                val randomBeachConditions = BeachConditions(
                    timeUpdated = listOf(1L, 2, 3, 4, 5).shuffled().first(),
                    _flagColor = listOf("Green", "Red", "Purple", "Yellow").shuffled().first(),
                    surfCondition = listOf("Rough", "Calm", "Moderate").shuffled().first(),
                    surfHeight = listOf("1 ft", "2 ft", "3, ft").shuffled().first(),
                    respiratoryIrritation = listOf("Preset", "Not present").shuffled().first(),
                    jellyFish = listOf("Preset", "Not present").shuffled().first(),
                )

                db.beachConditionsDao().insertBeachConditions(randomBeachConditions)
            }
        }

//        readRandomBeachConditions.setOnClickListener {
//
//            lifecycleScope.launch {
//                val beachConditions = db.beachConditionsDao().getBeachConditions()
//
//                Timber.d("$beachConditions")
//            }
//        }
    }
}