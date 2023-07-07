package dev.haqim.moviesapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.haqim.moviesapp.data.remote.network.ApiConfig
import dev.haqim.moviesapp.data.remote.network.MovieService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule{

    @Provides
    @Singleton
    fun provideMovieService(
        apiConfig: ApiConfig
    ): MovieService{
        return apiConfig.createService(MovieService::class.java)
    }
}