package dev.haqim.moviesapp.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import dev.haqim.moviesapp.domain.usecase.GenreInteractor
import dev.haqim.moviesapp.domain.usecase.GenreUseCase
import dev.haqim.moviesapp.domain.usecase.MovieInteractor
import dev.haqim.moviesapp.domain.usecase.MovieUseCase
import dev.haqim.moviesapp.domain.usecase.ReviewInteractor
import dev.haqim.moviesapp.domain.usecase.ReviewUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Singleton
    @Binds
    abstract fun provideMovieUseCase(
        movieInteractor: MovieInteractor
    ): MovieUseCase

    @Singleton
    @Binds
    abstract fun provideGenreUseCase(
        genreInteractor: GenreInteractor
    ): GenreUseCase

    @Singleton
    @Binds
    abstract fun provideReviewUseCase(
        reviewInteractor: ReviewInteractor
    ): ReviewUseCase

}