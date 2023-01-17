package com.cd.rounded.di

import com.cd.rounded.backend.StarlingService
import com.cd.rounded.domain.RoundUpRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideRoundUpRepo(
        starlingService: StarlingService
    ) : RoundUpRepo = RoundUpRepo(starlingService)
}