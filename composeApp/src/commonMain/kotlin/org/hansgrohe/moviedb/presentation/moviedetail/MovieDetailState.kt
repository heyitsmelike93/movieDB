package org.hansgrohe.moviedb.presentation.moviedetail

import com.example.shared.domain.model.MovieDetail

sealed interface MovieDetailUiState {
    data object Loading : MovieDetailUiState
    data class Success(val movieDetail: MovieDetail) : MovieDetailUiState
    data class Error(val message: String) : MovieDetailUiState
}
