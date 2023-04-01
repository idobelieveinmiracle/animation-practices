package com.rose.animationpractices.domain.repository

import com.rose.animationpractices.domain.data_source.DataSource
import com.rose.animationpractices.domain.entity.Food
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    fun getFoods(): Flow<DataSource<List<Food>>>
    suspend fun getFood(id: Long): DataSource<Food>
    suspend fun delete(id: Long): DataSource<Boolean>
    suspend fun update(food: Food): DataSource<Food>
    suspend fun add(food: Food): DataSource<Food>
}