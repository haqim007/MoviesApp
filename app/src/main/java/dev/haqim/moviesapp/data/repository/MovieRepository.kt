package dev.haqim.moviesapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import dev.haqim.moviesapp.data.local.datasource.MovieLocalDatasource
import dev.haqim.moviesapp.data.local.entity.toModel
import dev.haqim.moviesapp.data.mechanism.NetworkBoundResource
import dev.haqim.moviesapp.data.mechanism.Resource
import dev.haqim.moviesapp.data.pagingsource.MoviePagingSource
import dev.haqim.moviesapp.data.pagingsource.ReviewPagingSource
import dev.haqim.moviesapp.data.remote.RemoteDataSource
import dev.haqim.moviesapp.data.remote.response.GenreListResponse
import dev.haqim.moviesapp.data.remote.response.MovieDetailResponse
import dev.haqim.moviesapp.data.remote.response.toModel
import dev.haqim.moviesapp.data.remotemediator.MovieListRemoteMediator
import dev.haqim.moviesapp.data.util.DEFAULT_PAGE_SIZE
import dev.haqim.moviesapp.di.DispatcherIO
import dev.haqim.moviesapp.domain.model.Genre
import dev.haqim.moviesapp.domain.model.Movie
import dev.haqim.moviesapp.domain.model.MovieListItem
import dev.haqim.moviesapp.domain.model.ReviewItem
import dev.haqim.moviesapp.domain.repository.IMoviesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MoviesRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDatasource: MovieLocalDatasource,
    @DispatcherIO
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): IMoviesRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getMovies(
        genres: List<Genre>,
        genresOperator: Genre.GenresOperator?
    ): Flow<PagingData<MovieListItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false,
                maxSize = 3 * DEFAULT_PAGE_SIZE
            ),
            remoteMediator = MovieListRemoteMediator(
                localDatasource,
                remoteDataSource, genres, genresOperator
            ),
            pagingSourceFactory = {
                localDatasource.getPaging()
            }
        ).flow.map { pagingData ->
            pagingData.map { 
                it.toModel()
            }
        }.flowOn(dispatcher)
    }

    override fun getMovieDetail(movieId: Int): Flow<Resource<Movie>> {
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