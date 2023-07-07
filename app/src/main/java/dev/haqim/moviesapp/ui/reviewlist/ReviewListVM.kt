package dev.haqim.moviesapp.ui.reviewlist

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.haqim.moviesapp.domain.model.Movie
import dev.haqim.moviesapp.domain.model.ReviewItem
import dev.haqim.moviesapp.domain.usecase.ReviewUseCase
import dev.haqim.moviesapp.ui.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ReviewListVM @Inject constructor(
    private val useCase: ReviewUseCase
): BaseViewModel<ReviewsListUiState, ReviewsListUiAction, Nothing>() {

    private var _pagingDataFlow: Flow<PagingData<ReviewItem>>

    val pagingDataFlow: Flow<PagingData<ReviewItem>>
        get() = _pagingDataFlow

    override val _state = MutableStateFlow(ReviewsListUiState())

    init {
        val reviews = actionStateFlow
            .filterIsInstance<ReviewsListUiAction.FetchReviews>()
            .onStart {
                emit(ReviewsListUiAction.FetchReviews)
            }
        
        _pagingDataFlow = reviews.flatMapLatest{
            state.value.movie?.let {
                fetchReviews(it.id)
            } ?: flowOf(PagingData.empty())
        }.cachedIn(viewModelScope)

        actionStateFlow.updateStates().launchIn(viewModelScope)

    }

    override fun MutableSharedFlow<ReviewsListUiAction>.updateStates() = onEach {
        when(it){
            is ReviewsListUiAction.SetMovie -> setMovie(it.movie)
            else -> {}
        }
    }
    private fun fetchReviews(movieId: Int): Flow<PagingData<ReviewItem>>{
        return useCase.getReviews(movieId)
    }
    
    private fun setMovie(movie: Movie){
        _state.update { state ->
            state.copy(
                movie = movie
            )
        }
    }
}

data class ReviewsListUiState(
    val movie: Movie? = null
)

sealed class ReviewsListUiAction{
    data class SetMovie(val movie: Movie): ReviewsListUiAction()
    object FetchReviews: ReviewsListUiAction()
}