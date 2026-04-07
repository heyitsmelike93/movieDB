package com.example.shared.presentation.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.domain.usecase.GetPopularMoviesUseCase
import com.example.shared.domain.usecase.ObserveFavoriteMovieIdsUseCase
import com.example.shared.domain.usecase.ToggleFavoriteMovieUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val getPopularMovies: GetPopularMoviesUseCase,
    private val observeFavoriteMovieIds: ObserveFavoriteMovieIdsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteMovieUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieListUiState>(MovieListUiState.Loading)

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    val uiState: StateFlow<MovieListUiState> = combine(
        _uiState,
        observeFavoriteMovieIds()
    ) { data, favoriteIds ->
        if (data is MovieListUiState.Success)
            data.copy(movies = data.movies.map { it.copy(isFavorite = it.id in favoriteIds) })
        else data
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MovieListUiState.Loading
    )

    init { fetchPopularMovies() }

    private fun fetchPopularMovies() {
        viewModelScope.launch {
            getPopularMovies(page = 1).fold(
                onSuccess = { page ->
                    _uiState.value = MovieListUiState.Success(
                        movies = page.movies,
                        currentPage = page.page,
                        totalPages = page.totalPages
                    )
                },
                onFailure = { error ->
                    _uiState.value = MovieListUiState.Error(
                        message = error.message ?: "Something went wrong"
                    )
                }
            )
        }
    }

    fun loadMovies() {
        if (_uiState.value is MovieListUiState.Loading) return
        _uiState.value = MovieListUiState.Loading
        fetchPopularMovies()
    }

    fun loadMore() {
        val current = _uiState.value as? MovieListUiState.Success ?: return
        if (current.currentPage >= current.totalPages || current.isLoadingMore) return
        _uiState.value = current.copy(isLoadingMore = true, loadMoreError = null)
        viewModelScope.launch {
            getPopularMovies(page = current.currentPage + 1).fold(
                onSuccess = { page ->
                    val latest = _uiState.value as? MovieListUiState.Success ?: return@fold
                    _uiState.value = latest.copy(
                        movies = latest.movies + page.movies,
                        currentPage = page.page,
                        totalPages = page.totalPages,
                        isLoadingMore = false
                    )
                },
                onFailure = { error ->
                    val latest = _uiState.value as? MovieListUiState.Success ?: return@fold
                    _uiState.value = latest.copy(
                        isLoadingMore = false,
                        loadMoreError = error.message ?: "Failed to load more"
                    )
                }
            )
        }
    }

    fun retryLoadMore() {
        val current = _uiState.value as? MovieListUiState.Success ?: return
        _uiState.value = current.copy(loadMoreError = null)
        loadMore()
    }

    fun toggleFavorite(movieId: Int) {
        viewModelScope.launch {
            runCatching { toggleFavoriteUseCase(movieId) }
                .onFailure { _toastMessage.emit("Failed to update favorite") }
        }
    }


}
