package com.rose.animationpractices.domain.use_case

import com.rose.animationpractices.domain.data_helper.ImageHelper
import com.rose.animationpractices.domain.data_source.DataSource
import com.rose.animationpractices.domain.entity.Food
import com.rose.animationpractices.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFoods(private val repository: FoodRepository, private val imageHelper: ImageHelper) {
    operator fun invoke(): Flow<DataSource<List<Food>>> {
        return repository.getFoods().map {
            if (it is DataSource.Success && it.data != null) {
                DataSource.Success(it.data.map { food ->
                    food.copy(
                        imageUri = imageHelper.getImageUri(
                            food.imageFileName
                        )
                    )
                }.sortedByDescending { food -> food.createdTime })
            } else {
                it
            }
        }
    }
}