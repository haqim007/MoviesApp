package dev.haqim.moviesapp.ui.moviedetail

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.haqim.moviesapp.data.mechanism.Resource
import dev.haqim.moviesapp.domain.model.Movie
import dev.haqim.moviesapp.domain.model.MovieListItem
import dev.haqim.moviesapp.domain.usecase.MovieUseCase
import dev.haqim.moviesapp.ui.BaseViewModel
import dev.haqim.moviesapp.ui.movielist.MovieListState
import dev.haqim.moviesapp.ui.movielist.MovieListUiAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailVM @Inject constructor (
    private val movieUseCase: MovieUseCase
): BaseViewModel<MovieDetailUiState, MovieDetailUiAction, Nothing>() {

    override val _state = MutableStateFlow(MovieDetailUiState())

    init {
        actionStateFlow.updateStates().launchIn(viewModelScope)
    }

    override fun MutableSharedFlow<MovieDetailUiAction>.updateStates() = onEach {
        when(it){
            is MovieDetailUiAction.FetchMovie -> fetchMovie(it.movieId)
        }
    }

    private fun fetchMovie(movieId: Int){
        viewModelScope.launch {
            movieUseCase.getMovie(movieId).collect{
                _state.update { state ->
                    state.copy(
                        movie = it
                    )
                }
            }
        }
    }
}

data class MovieDetailUiState(
    val movie: Resource<Movie> = Resource.Idle()
)
sealed class MovieDetailUiAction{
    data class FetchMovie(val movieId: Int): MovieDetailUiAction()
}