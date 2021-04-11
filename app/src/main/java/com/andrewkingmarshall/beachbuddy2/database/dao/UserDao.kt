package com.andrewkingmarshall.beachbuddy2.database.dao

import androidx.room.*
import com.andrewkingmarshall.beachbuddy2.database.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(score: Score)

    @Transaction
    @Query("SELECT * FROM User")
    fun getUsersWithScores(): Flow<UserWithScores>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequestedItem(requestedItem: RequestedItem)

    @Query("SELECT * FROM RequestedItem")
    fun getRequestedItems(): Flow<List<RequestedItem>>

}