package com.example.shared.presentation.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.domain.usecase.GetMovieDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val getMovieDetail: GetMovieDetailUseCase,
    private val movieId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    init { startLoad() }

    fun loadMovieDetail() {
        if (_uiState.value is MovieDetailUiState.Loading) return
        _uiState.value = MovieDetailUiState.Loading
        startLoad()
    }

    private fun startLoad() {
        viewModelScope.launch {
            getMovieDetail(movieId).fold(
                onSuccess = { detail ->
                    _uiState.value = MovieDetailUiState.Success(movieDetail = detail)
                },
                onFailure = { error ->
                    _uiState.value = MovieDetailUiState.Error(
                        message = error.message ?: "Something went wrong"
                    )
                }
            )
        }
    }
}
