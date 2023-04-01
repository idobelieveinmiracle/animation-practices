package com.rose.animationpractices.domain.use_case

import com.rose.animationpractices.domain.data_helper.ImageHelper
import com.rose.animationpractices.domain.data_source.DataSource
import com.rose.animationpractices.domain.entity.Food
import com.rose.animationpractices.domain.repository.FoodRepository

class AddFood(private val repository: FoodRepository, private val imageHelper: ImageHelper) {
    suspend operator fun invoke(food: Food): DataSource<Food> {
        val imageFileName = if (food.imageUri.isNotEmpty()) {
            imageHelper.saveUriToFile(food.imageUri)
        } else {
            ""
        }
        return repository.add(food.copy(imageFileName = imageFileName))
    }
}