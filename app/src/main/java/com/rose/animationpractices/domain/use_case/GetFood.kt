package com.rose.animationpractices.domain.use_case

import com.rose.animationpractices.domain.data_helper.ImageHelper
import com.rose.animationpractices.domain.data_source.DataSource
import com.rose.animationpractices.domain.entity.Food
import com.rose.animationpractices.domain.repository.FoodRepository

class GetFood(private val foodRepository: FoodRepository, private val imageHelper: ImageHelper) {
    suspend operator fun invoke(foodId: Long): DataSource<Food> {
        val dataSource = foodRepository.getFood(foodId)
        if (dataSource is DataSource.Success && dataSource.data != null) {
            val imageUri = if (dataSource.data.imageFileName.isNotEmpty()) {
                imageHelper.getImageUri(dataSource.data.imageFileName)
            } else {
                ""
            }
            return DataSource.Success(dataSource.data.copy(imageUri = imageUri))
        }
        return dataSource
    }
}