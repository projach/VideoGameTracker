package com.projach.videogametracker.di

import com.projach.videogametracker.data.VideoGamesRepositoryImpl
import com.projach.videogametracker.domain.VideoGamesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class VideoGamesTrackerModule {
    @Binds
    @Singleton
    abstract fun bindVideoGamesRepository(
        impl: VideoGamesRepositoryImpl
    ): VideoGamesRepository
}