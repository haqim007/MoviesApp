package dev.haqim.moviesapp.domain.usecase

import androidx.paging.PagingData
import dev.haqim.moviesapp.data.mechanism.Resource
import dev.haqim.moviesapp.data.repository.MoviesRepository
import dev.haqim.moviesapp.domain.model.Genre
import dev.haqim.moviesapp.domain.model.Movie
import dev.haqim.moviesapp.domain.model.MovieListItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieUseCase @Inject constructor (
    private val repo: MoviesRepository
) {
    operator fun invoke(movieId: Int): Flow<Resource<Movie>> {
        return repo.getMovieDetail(movieId)
    }
}