package com.example.shared.presentation.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.domain.usecase.GetMovieDetailUseCase
import com.example.shared.domain.usecase.ObserveMovieFavoriteStatusUseCase
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

class MovieDetailViewModel(
    private val getMovieDetail: GetMovieDetailUseCase,
    private val observeMovieFavoriteStatus: ObserveMovieFavoriteStatusUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteMovieUseCase,
    private val movieId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    val uiState: StateFlow<MovieDetailUiState> = combine(
        _uiState,
        observeMovieFavoriteStatus(movieId)
    ) { data, isFavorite ->
        if (data is MovieDetailUiState.Success)
            data.copy(movieDetail = data.movieDetail.copy(isFavorite = isFavorite))
        else data
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MovieDetailUiState.Loading
    )

    init { fetchMovieDetail() }

    private fun fetchMovieDetail() {
        viewModelScope.launch {
            getMovieDetail(movieId).fold(
                onSuccess = { detail ->
                    _uiState.value = MovieDetailUiState.Success(detail)
                },
                onFailure = { error ->
                    _uiState.value = MovieDetailUiState.Error(
                        message = error.message ?: "Something went wrong"
                    )
                }
            )
        }
    }

    fun loadMovieDetail() {
        if (_uiState.value is MovieDetailUiState.Loading) return
        _uiState.value = MovieDetailUiState.Loading
        fetchMovieDetail()
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            runCatching { toggleFavoriteUseCase(movieId) }
                .onFailure { _toastMessage.emit("Failed to update favorite") }
        }
    }

}
