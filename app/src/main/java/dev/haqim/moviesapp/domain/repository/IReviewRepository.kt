package dev.haqim.moviesapp.domain.repository

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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton


interface IReviewRepository {

    fun getTopReviews(
        movieId: Int,
        size: Int = 3
    ): Flow<Resource<List<ReviewItem>>> 

    fun getReviews(
        movieId: Int,
    ): Flow<PagingData<ReviewItem>>

}