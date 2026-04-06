package org.hansgrohe.moviedb.presentation.moviedetail

import com.example.shared.domain.model.MovieDetail

data class MovieDetailState(
    val movieDetail: MovieDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
