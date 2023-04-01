package com.rose.animationpractices.di.application

import android.content.Context
import com.rose.animationpractices.data.data_helper.ImageHelperImpl
import com.rose.animationpractices.domain.data_helper.ImageHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    fun provideImageHelper(@ApplicationContext context: Context): ImageHelper {
        return ImageHelperImpl(context)
    }
}