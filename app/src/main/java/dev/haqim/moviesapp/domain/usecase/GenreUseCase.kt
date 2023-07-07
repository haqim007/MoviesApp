package dev.haqim.moviesapp.domain.usecase

import dev.haqim.moviesapp.data.mechanism.Resource
import dev.haqim.moviesapp.domain.model.Genre
import kotlinx.coroutines.flow.Flow

interface GenreUseCase {
    fun getGenres(): Flow<Resource<List<Genre>>>
    suspend fun toggleCheckedGenre(selectedGenre: Genre, genres: Resource<List<Genre>>): Resource<List<Genre>>
}