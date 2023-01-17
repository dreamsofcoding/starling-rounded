package com.cd.rounded.di

import com.cd.rounded.backend.Retrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Di {

    @Singleton
    @Provides
    fun provideOkHttpClient(
    ) = OkHttpClient.Builder().build()

    @Singleton
    @Provides
    fun provideStarlingService(okHttpClient: OkHttpClient) =
        Retrofit(okHttpClient).createStarlingService()
}