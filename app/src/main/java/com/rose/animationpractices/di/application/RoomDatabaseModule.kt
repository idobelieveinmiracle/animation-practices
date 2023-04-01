package com.rose.animationpractices.di.application

import android.content.Context
import androidx.room.Room
import com.rose.animationpractices.data.remote.AppDatabase
import com.rose.animationpractices.data.remote.FoodDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomDatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app-database"
        ).build()
    }

    @Provides
    @Singleton
    fun providesFoodDao(appDatabase: AppDatabase): FoodDao {
        return appDatabase.foodDao()
    }
}