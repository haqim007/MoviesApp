package dev.haqim.moviesapp.ui.movielist

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.haqim.moviesapp.data.mechanism.Resource
import dev.haqim.moviesapp.domain.model.Genre
import dev.haqim.moviesapp.domain.model.MovieListItem
import dev.haqim.moviesapp.domain.usecase.FetchGenreUseCase
import dev.haqim.moviesapp.domain.usecase.FetchMoviesUseCase
import dev.haqim.moviesapp.domain.usecase.ToggleGenreUseCase
import dev.haqim.moviesapp.ui.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MovieListVM @Inject constructor(
    private val toggleGenreUseCase: ToggleGenreUseCase,
    private val fetchGenreUseCase: FetchGenreUseCase,
    private val fetchMoviesUseCase: FetchMoviesUseCase
): BaseViewModel<MovieListState, MovieListUiAction, Nothing>() {

    private var _pagingDataFlow: Flow<PagingData<MovieListItem>>

    val pagingDataFlow: Flow<PagingData<MovieListItem>>
        get() = _pagingDataFlow

    override val _state = MutableStateFlow(MovieListState())

    init {
        val movies = actionStateFlow
            .filterIsInstance<MovieListUiAction.FetchMovies>()
            .onStart {
                emit(MovieListUiAction.FetchMovies)
            }

        _pagingDataFlow = movies.flatMapLatest{
            fetchMovies(state.value.activeGenres)
        }.cachedIn(viewModelScope)

        actionStateFlow.updateStates().launchIn(viewModelScope)

        // fetch genres for at initialization
        processAction(MovieListUiAction.FetchGenres)

    }

    override fun MutableSharedFlow<MovieListUiAction>.updateStates() = onEach {
        when(it){
            is MovieListUiAction.FetchGenres -> fetchGenres()
            is MovieListUiAction.OnClickGenre -> onClickGenre(it.genre)
            else -> {}
        }
    }

    private fun onClickGenre(genre: Genre) {
        viewModelScope.launch {
            val newGenresData = toggleGenreUseCase(genre, state.value.genres)
            _state.update { state ->
                state.copy(
                    activeGenres = if(genre.isChecked)
                        state.activeGenres.filter {
                            it.id != genre.id
                        }
                    else
                        state.activeGenres + genre.copy(isChecked = true),
                    genres = newGenresData
                )
            }
        }
    }

    private fun fetchGenres(){
        viewModelScope.launch {
            fetchGenreUseCase().collect{
                _state.update { state ->
                    state.copy(
                        genres = it,
                        activeGenres = listOf()
                    )
                }
            }
        }
    }

    private fun fetchMovies(activeGenres: List<Genre>):  Flow<PagingData<MovieListItem>>{
        return fetchMoviesUseCase(activeGenres)
    }
}

data class MovieListState(
    val genres: Resource<List<Genre>> = Resource.Idle(),
    val activeGenres: List<Genre> = listOf()
)

sealed interface MovieListUiAction{
    object FetchGenres: MovieListUiAction
    data class OnClickGenre(val genre: Genre): MovieListUiAction
    object FetchMovies: MovieListUiAction
}