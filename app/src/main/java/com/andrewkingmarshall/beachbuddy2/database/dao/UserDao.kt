package com.andrewkingmarshall.beachbuddy2.database.dao

import androidx.room.*
import com.andrewkingmarshall.beachbuddy2.database.model.RequestedItem
import com.andrewkingmarshall.beachbuddy2.database.model.Score
import com.andrewkingmarshall.beachbuddy2.database.model.User
import com.andrewkingmarshall.beachbuddy2.database.model.UserWithScores
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(score: Score)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScores(scores: List<Score>)

    @Transaction
    @Query("SELECT * FROM User")
    fun getUsersWithScores(): Flow<List<UserWithScores>>

    @Query("SELECT * FROM Score WHERE scoreId = :scoreId")
    fun getScore(scoreId: String) : Flow<Score>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequestedItem(requestedItem: RequestedItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequestedItems(requestedItems: List<RequestedItem>)

    @Delete
    suspend fun deleteRequestedItems(itemsToDelete: List<RequestedItem>)

    @Query("SELECT * FROM RequestedItem WHERE isComplete = 0 ORDER BY createdAtTime DESC")
    fun getNotCompletedRequestedItems(): Flow<List<RequestedItem>>

    @Query("SELECT * FROM RequestedItem WHERE isComplete = 1 AND completedAtTime BETWEEN :todayStartOfDay AND :tomorrowStartOfDayMillis ORDER BY completedAtTime DESC")
    fun getCompletedTodayRequestedItems(
        todayStartOfDay: Long,
        tomorrowStartOfDayMillis: Long,
    ): Flow<List<RequestedItem>>

    @Query("SELECT * FROM RequestedItem WHERE isComplete = 1 AND completedAtTime < :todayStartOfDay")
    fun getOldCompletedRequestedItems(
        todayStartOfDay: Long,
    ): Flow<List<RequestedItem>>

}