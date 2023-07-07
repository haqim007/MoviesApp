package dev.haqim.moviesapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.haqim.moviesapp.data.mechanism.NetworkBoundResource
import dev.haqim.moviesapp.data.mechanism.Resource
import dev.haqim.moviesapp.data.pagingsource.MoviePagingSource
import dev.haqim.moviesapp.data.pagingsource.ReviewPagingSource
import dev.haqim.moviesapp.data.remote.RemoteDataSource
import dev.haqim.moviesapp.data.remote.response.GenreListResponse
import dev.haqim.moviesapp.data.remote.response.MovieDetailResponse
import dev.haqim.moviesapp.data.remote.response.toModel
import dev.haqim.moviesapp.data.util.DEFAULT_PAGE_SIZE
import dev.haqim.moviesapp.di.DispatcherIO
import dev.haqim.moviesapp.domain.model.Genre
import dev.haqim.moviesapp.domain.model.Movie
import dev.haqim.moviesapp.domain.model.MovieListItem
import dev.haqim.moviesapp.domain.model.ReviewItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MoviesRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    @DispatcherIO
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun getMovies(
        genres: List<Genre> = listOf(),
        genresOperator: Genre.GenresOperator? = null
    ): Flow<PagingData<MovieListItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false,
                maxSize = 3 * DEFAULT_PAGE_SIZE
            ),
            pagingSourceFactory = {
                MoviePagingSource(
                    remoteDataSource = remoteDataSource,
                    genres = genres,
                    genresOperator = genresOperator
                )
            }
        ).flow
    }

    fun getMovieDetail(movieId: Int): Flow<Resource<Movie>> {
        return object: NetworkBoundResource<Movie, MovieDetailResponse>(){
            override suspend fun requestFromRemote(): Result<MovieDetailResponse> {
                val trailerResponse = remoteDataSource.getMovieTrailer(movieId)
                val result = remoteDataSource.getMovieDetail(movieId)
                return if(result.isSuccess){
                    Result.success(
                        result.getOrThrow().copy(
                            trailerKey = trailerResponse.getOrThrow()?.key
                        )
                    )
                }else{
                    result
                }
            }

            override fun loadResult(data: MovieDetailResponse): Flow<Movie> {
                return flowOf(data.toModel())
            }
        }.asFlow().flowOn(dispatcher)
    }

}