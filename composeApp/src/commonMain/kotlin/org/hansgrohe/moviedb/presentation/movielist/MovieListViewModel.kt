package org.hansgrohe.moviedb.presentation.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MovieListState())
    val state: StateFlow<MovieListState> = _state.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies() {
        if (_state.value.isLoading) return
        _state.update { MovieListState(isLoading = true) }
        viewModelScope.launch {
            repository.getPopularMovies(page = 1).fold(
                onSuccess = { page ->
                    _state.update {
                        it.copy(
                            movies = page.movies,
                            isLoading = false,
                            currentPage = page.page,
                            totalPages = page.totalPages,
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isLoading = false, error = error.message ?: "Something went wrong")
                    }
                }
            )
        }
    }

    fun loadMore() {
        val current = _state.value
        if (!current.canLoadMore) return
        _state.update { it.copy(isLoadingMore = true) }
        viewModelScope.launch {
            repository.getPopularMovies(page = current.currentPage + 1).fold(
                onSuccess = { page ->
                    _state.update {
                        it.copy(
                            movies = it.movies + page.movies,
                            isLoadingMore = false,
                            currentPage = page.page,
                            totalPages = page.totalPages
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isLoadingMore = false, error = error.message ?: "Failed to load more")
                    }
                }
            )
        }
    }
}
