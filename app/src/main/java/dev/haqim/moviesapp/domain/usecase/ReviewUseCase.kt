package dev.haqim.moviesapp.domain.usecase

import androidx.paging.PagingData
import dev.haqim.moviesapp.data.mechanism.Resource
import dev.haqim.moviesapp.domain.model.ReviewItem
import kotlinx.coroutines.flow.Flow

interface ReviewUseCase {
    fun getTopThree(movieId: Int): Flow<Resource<List<ReviewItem>>>
    fun getReviews(movieId: Int): Flow<PagingData<ReviewItem>>
}