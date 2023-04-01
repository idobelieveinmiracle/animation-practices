package com.rose.animationpractices.view_model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rose.animationpractices.di.view_model_factory.AddFoodViewModelAssistedFactory
import com.rose.animationpractices.domain.entity.Food

class AddFoodViewModelFactory(
    private val assistedFactory: AddFoodViewModelAssistedFactory,
    private val originalFood: Food
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return assistedFactory.create(originalFood) as T
    }
}