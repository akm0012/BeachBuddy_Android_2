package com.andrewkingmarshall.beachbuddy2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andrewkingmarshall.beachbuddy2.database.dao.BeachConditionsDao
import com.andrewkingmarshall.beachbuddy2.database.model.BeachConditions

@Database(entities = arrayOf(BeachConditions::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun beachConditionsDao(): BeachConditionsDao
}