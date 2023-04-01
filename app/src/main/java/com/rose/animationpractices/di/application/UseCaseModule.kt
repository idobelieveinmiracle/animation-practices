package com.rose.animationpractices.di.application

import com.rose.animationpractices.domain.data_helper.ImageHelper
import com.rose.animationpractices.domain.repository.FoodRepository
import com.rose.animationpractices.domain.use_case.AddFood
import com.rose.animationpractices.domain.use_case.GetFood
import com.rose.animationpractices.domain.use_case.GetFoods
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    @Singleton
    fun provideGetFoods(foodRepository: FoodRepository, imageHelper: ImageHelper): GetFoods {
        return GetFoods(foodRepository, imageHelper)
    }

    @Provides
    @Singleton
    fun provideAddFood(repository: FoodRepository, imageHelper: ImageHelper): AddFood {
        return AddFood(repository, imageHelper)
    }

    @Provides
    @Singleton
    fun provideGetFood(repository: FoodRepository, imageHelper: ImageHelper): GetFood {
        return GetFood(repository, imageHelper)
    }
}