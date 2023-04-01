package com.rose.animationpractices.data.remote

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM food")
    fun getFoods(): Flow<List<FoodEntity>>

    @Query("SELECT * FROM food WHERE id = :id")
    suspend fun getFoods(id: Long): List<FoodEntity>

    @Insert
    suspend fun add(foodEntity: FoodEntity): Long

    @Update
    suspend fun update(foodEntity: FoodEntity): Int

    @Query("DELETE FROM food WHERE id = :id")
    suspend fun delete(id: Long): Int
}