package com.example.shared.presentation.movielist

import com.example.shared.domain.model.Movie

sealed interface MovieListUiState {

    data object Loading : MovieListUiState

    data class Success(
        val movies: List<Movie>,
        val currentPage: Int,
        val totalPages: Int,
        val isLoadingMore: Boolean = false,
        val loadMoreError: String? = null
    ) : MovieListUiState {
        val canLoadMore: Boolean
            get() = currentPage < totalPages && !isLoadingMore && loadMoreError == null
    }

    data class Error(val message: String) : MovieListUiState
}
