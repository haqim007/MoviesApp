package dev.haqim.moviesapp.data.remote.network

import dev.haqim.moviesapp.data.remote.response.GenreListResponse
import dev.haqim.moviesapp.data.remote.response.MovieDetailResponse
import dev.haqim.moviesapp.data.remote.response.ReviewsResponse
import dev.haqim.moviesapp.data.remote.response.MovieListResponse
import dev.haqim.moviesapp.data.remote.response.MovieTrailersResponse
import retrofit2.http.*

interface MovieService {
    @GET("discover/{path}")
    suspend fun getMovies(
        @Path("path") path: String = "movie",
        @Query("page") page: Int,
        @Query("with_genres", encoded = true) genres: String
    ): MovieListResponse

    @GET("genre/{path}/list")
    suspend fun getGenres(
        @Path("path") path: String = "movie",
    ): GenreListResponse

    @GET("movie/{movie_id}/reviews")
    suspend fun getReviews(
        @Path("movie_id") id: Int,
        @Query("page") page: Int
    ): ReviewsResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
    ): MovieDetailResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
    ): MovieTrailersResponse


}