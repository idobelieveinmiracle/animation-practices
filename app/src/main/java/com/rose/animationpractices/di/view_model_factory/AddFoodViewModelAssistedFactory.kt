package com.rose.animationpractices.di.view_model_factory

import com.rose.animationpractices.domain.entity.Food
import com.rose.animationpractices.view_model.AddFoodViewModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface AddFoodViewModelAssistedFactory {
    fun create(originalFood: Food): AddFoodViewModel
}