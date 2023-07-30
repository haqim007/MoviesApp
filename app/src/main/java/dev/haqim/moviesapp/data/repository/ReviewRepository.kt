package dev.haqim.moviesapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.haqim.moviesapp.data.mechanism.NetworkBoundResource
import dev.haqim.moviesapp.data.mechanism.Resource
import dev.haqim.moviesapp.data.pagingsource.ReviewPagingSource
import dev.haqim.moviesapp.data.remote.RemoteDataSource
import dev.haqim.moviesapp.data.remote.response.ReviewsResponse
import dev.haqim.moviesapp.data.remote.response.toModel
import dev.haqim.moviesapp.data.util.DEFAULT_PAGE_SIZE
import dev.haqim.moviesapp.di.DispatcherIO
import dev.haqim.moviesapp.domain.model.ReviewItem
import dev.haqim.moviesapp.domain.repository.IReviewRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    @DispatcherIO
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): IReviewRepository {

    override fun getTopReviews(
        movieId: Int,
        size: Int
    ): Flow<Resource<List<ReviewItem>>> {
        return object : NetworkBoundResource<List<ReviewItem>, ReviewsResponse>(){
            override suspend fun requestFromRemote(): Result<ReviewsResponse> {
                return remoteDataSource.getMovieReviews(movieId = movieId)
            }

            override fun loadResult(data: ReviewsResponse): Flow<List<ReviewItem>> {
                return flowOf(data.toModel().subList(0, size - 1))
            }
        }.asFlow().flowOn(dispatcher)
    }

    override fun getReviews(
        movieId: Int,
    ): Flow<PagingData<ReviewItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false,
                maxSize = 3 * DEFAULT_PAGE_SIZE
            ),
            pagingSourceFactory = {
                ReviewPagingSource(
                    remoteDataSource = remoteDataSource,
                    movieId = movieId
                )
            }
        ).flow
    }

}