package dev.haqim.moviesapp.domain.usecase

import androidx.paging.PagingData
import dev.haqim.moviesapp.data.mechanism.Resource
import dev.haqim.moviesapp.data.repository.ReviewRepository
import dev.haqim.moviesapp.domain.model.ReviewItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopReviewsUseCase @Inject constructor (
    private val repo: ReviewRepository
) {
    operator fun invoke(movieId: Int): Flow<Resource<List<ReviewItem>>> {
        return repo.getTopReviews(movieId)
    }
}