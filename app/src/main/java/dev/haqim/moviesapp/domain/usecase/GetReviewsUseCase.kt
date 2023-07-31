package dev.haqim.moviesapp.domain.usecase

import androidx.paging.PagingData
import dev.haqim.moviesapp.data.mechanism.Resource
import dev.haqim.moviesapp.data.repository.ReviewRepository
import dev.haqim.moviesapp.domain.model.ReviewItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReviewsUseCase @Inject constructor (
    private val repo: ReviewRepository
){
    operator fun invoke(movieId: Int): Flow<PagingData<ReviewItem>> {
        return repo.getReviews(movieId)
    }
}