package dev.haqim.moviesapp.domain.repository

import androidx.paging.PagingData
import dev.haqim.moviesapp.data.mechanism.Resource
import dev.haqim.moviesapp.domain.model.Genre
import dev.haqim.moviesapp.domain.model.Movie
import dev.haqim.moviesapp.domain.model.MovieListItem
import kotlinx.coroutines.flow.Flow


interface IMoviesRepository{

    fun getMovies(
        genres: List<Genre> = listOf(),
        genresOperator: Genre.GenresOperator? = null
    ): Flow<PagingData<MovieListItem>>

    fun getMovieDetail(movieId: Int): Flow<Resource<Movie>>

}