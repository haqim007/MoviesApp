package dev.haqim.moviesapp.domain.usecase

import androidx.paging.PagingData
import dev.haqim.moviesapp.data.mechanism.Resource
import dev.haqim.moviesapp.data.repository.ReviewRepository
import dev.haqim.moviesapp.domain.model.ReviewItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReviewInteractor @Inject constructor (
    private val repo: ReviewRepository
): ReviewUseCase {
    override fun getTopThree(movieId: Int): Flow<Resource<List<ReviewItem>>> {
        return repo.getTopReviews(movieId)
    }

    override fun getReviews(movieId: Int): Flow<PagingData<ReviewItem>> {
        return repo.getReviews(movieId)
    }
}