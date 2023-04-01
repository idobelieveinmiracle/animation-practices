package com.rose.animationpractices.data.repository

import android.util.Log
import com.rose.animationpractices.data.remote.FoodDao
import com.rose.animationpractices.data.remote.FoodEntity
import com.rose.animationpractices.domain.data_source.CanNotUpdateException
import com.rose.animationpractices.domain.data_source.DataSource
import com.rose.animationpractices.domain.data_source.IdNotMatchedException
import com.rose.animationpractices.domain.entity.Food
import com.rose.animationpractices.domain.repository.FoodRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class FoodRepositoryImpl(private val foodDao: FoodDao) : FoodRepository {

    companion object {
        private const val TAG = "FoodRepositoryImpl"
    }

    override fun getFoods(): Flow<DataSource<List<Food>>> {
        Log.i(TAG, "getFoods: ")
        return foodDao.getFoods()
            .map { foods ->
                Log.i(TAG, "getFoods: loaded food")
                DataSource.Success(foods.map { food -> food.toFood() }) as DataSource<List<Food>>
            }.onStart {
//                emit(DataSource.Loading())
//                delay(2000)
                Log.i(TAG, "getFoods: start")
            }
    }

    override suspend fun getFood(id: Long): DataSource<Food> {
        val foods = foodDao.getFoods(id)
        if (foods.isEmpty()) {
            return DataSource.Failure(exception = IdNotMatchedException(id.toString()))
        }

        return DataSource.Success(foods.first().toFood())
    }

    override suspend fun delete(id: Long): DataSource<Boolean> {
        if (foodDao.delete(id) > 0) {
            return DataSource.Success(true)
        }

        return DataSource.Failure(exception = CanNotUpdateException("[id: $id]"))
    }

    override suspend fun update(food: Food): DataSource<Food> {
        val updatedRow = foodDao.update(FoodEntity.fromFood(food))

        if (updatedRow > 0) {
            return DataSource.Success(food)
        }

        return DataSource.Failure(exception = CanNotUpdateException("[id: ${food.id}"))
    }

    override suspend fun add(food: Food): DataSource<Food> {
        val addedId = foodDao.add(FoodEntity.fromFood(food))

        if (addedId > 0) {
            return DataSource.Success(food.copy(id = addedId))
        }

        return DataSource.Failure(exception = CanNotUpdateException("[id: ${food.id}]"))
    }
}