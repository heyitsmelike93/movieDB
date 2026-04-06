package org.hansgrohe.moviedb.presentation.movielist

import com.example.shared.domain.model.Movie

data class MovieListState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 0,
    val totalPages: Int = 1
) {
    val canLoadMore: Boolean
        get() = currentPage < totalPages && !isLoading && !isLoadingMore
}
