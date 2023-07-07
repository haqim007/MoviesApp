package dev.haqim.moviesapp.data.remote

import dev.haqim.moviesapp.data.remote.network.MovieService
import dev.haqim.moviesapp.data.remote.response.GenreListResponse
import dev.haqim.moviesapp.data.remote.response.MovieDetailResponse
import dev.haqim.moviesapp.data.remote.response.MovieListResponse
import dev.haqim.moviesapp.data.remote.response.MovieTrailersResponse
import dev.haqim.moviesapp.data.remote.response.ReviewsResponse
import dev.haqim.moviesapp.data.util.getResult
import dev.haqim.moviesapp.domain.model.Genre
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val service: MovieService
) {

    suspend fun getGenres(): Result<GenreListResponse>{
        return getResult {
            service.getGenres()
        }
    }

    suspend fun getMovies(
        page: Int,
        genres: List<Genre> = listOf(),
        genresOperator: Genre.GenresOperator? = null
    ): Result<MovieListResponse>{
        val genresParam = if (genres.size > 1){
            genresOperator?.let {
                genres.joinToString(it.separator) { genre -> genre.id.toString() }
            } ?: genres.joinToString("|") { it.id.toString() }
        }else if(genres.size == 1){
            genres[0].id.toString()
        }else ""

        return getResult {
            service.getMovies(page = page, genres = genresParam)
        }
    }

    suspend fun getMovieDetail(movieId: Int): Result<MovieDetailResponse> = getResult {
        service.getMovieDetail(movieId)
    }

    suspend fun getMovieTrailer(movieId: Int): Result<MovieTrailersResponse.TrailerResponse?> = getResult {
        service.getMovieVideos(movieId).results.first()
    }

    suspend fun getMovieReviews(movieId: Int, page: Int): Result<ReviewsResponse> = getResult {
        service.getReviews(movieId, page)
    }

    suspend fun getMovieReviews(movieId: Int): Result<ReviewsResponse> = getResult {
        service.getReviews(movieId, 1)
    }

}