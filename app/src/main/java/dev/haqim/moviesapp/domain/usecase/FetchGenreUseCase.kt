package dev.haqim.moviesapp.domain.usecase

import dev.haqim.moviesapp.data.mechanism.Resource
import dev.haqim.moviesapp.data.repository.GenreRepository
import dev.haqim.moviesapp.di.DispatcherDefault
import dev.haqim.moviesapp.domain.model.Genre
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchGenreUseCase @Inject constructor(
    private val repo: GenreRepository,
) {
    operator fun invoke(): Flow<Resource<List<Genre>>> {
        return repo.getGenres()
    }
}