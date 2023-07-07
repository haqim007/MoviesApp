package dev.haqim.moviesapp.domain.usecase

import androidx.paging.PagingData
import dev.haqim.moviesapp.data.mechanism.Resource
import dev.haqim.moviesapp.data.repository.MoviesRepository
import dev.haqim.moviesapp.domain.model.Genre
import dev.haqim.moviesapp.domain.model.Movie
import dev.haqim.moviesapp.domain.model.MovieListItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieInteractor @Inject constructor (
    private val repo: MoviesRepository
): MovieUseCase {
    override fun getMovies(genres: List<Genre>): Flow<PagingData<MovieListItem>> {
        return repo.getMovies(genres = genres)
    }

    override fun getMovie(movieId: Int): Flow<Resource<Movie>> {
        return repo.getMovieDetail(movieId)
    }
}