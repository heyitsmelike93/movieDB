package org.hansgrohe.moviedb.presentation.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.domain.usecase.GetMovieDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val getMovieDetail: GetMovieDetailUseCase,
    private val movieId: Int
) : ViewModel() {

    private val _state = MutableStateFlow(MovieDetailState())
    val state: StateFlow<MovieDetailState> = _state.asStateFlow()

    init {
        loadMovieDetail()
    }

    fun loadMovieDetail() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getMovieDetail(movieId).fold(
                onSuccess = { detail ->
                    _state.update { it.copy(isLoading = false, movieDetail = detail) }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isLoading = false, error = error.message ?: "Something went wrong")
                    }
                }
            )
        }
    }
}
