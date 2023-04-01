package com.rose.animationpractices.di.application

import com.rose.animationpractices.data.remote.FoodDao
import com.rose.animationpractices.data.repository.FoodRepositoryImpl
import com.rose.animationpractices.domain.repository.FoodRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideFoodRepository(foodDao: FoodDao): FoodRepository {
        return FoodRepositoryImpl(foodDao)
    }
}