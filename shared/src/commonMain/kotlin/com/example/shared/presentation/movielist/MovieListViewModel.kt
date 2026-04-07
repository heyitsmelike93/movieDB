package com.example.shared.presentation.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val getPopularMovies: GetPopularMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieListUiState>(MovieListUiState.Loading)
    val uiState: StateFlow<MovieListUiState> = _uiState.asStateFlow()

    init { startLoad() }

    fun loadMovies() {
        if (_uiState.value is MovieListUiState.Loading) return
        _uiState.value = MovieListUiState.Loading
        startLoad()
    }

    fun loadMore() {
        val current = _uiState.value as? MovieListUiState.Success ?: return
        if (!current.canLoadMore) return
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

    private fun startLoad() {
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
}
