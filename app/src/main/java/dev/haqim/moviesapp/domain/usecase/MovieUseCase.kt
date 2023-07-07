package dev.haqim.moviesapp.domain.usecase

import androidx.paging.PagingData
import dev.haqim.moviesapp.data.mechanism.Resource
import dev.haqim.moviesapp.domain.model.Genre
import dev.haqim.moviesapp.domain.model.Movie
import dev.haqim.moviesapp.domain.model.MovieListItem
import kotlinx.coroutines.flow.Flow

interface MovieUseCase {
   fun getMovies(genres: List<Genre>): Flow<PagingData<MovieListItem>>

   fun getMovie(movieId: Int): Flow<Resource<Movie>>

}