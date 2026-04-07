package com.example.shared.domain.usecase

import com.example.shared.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class ObserveMovieFavoriteStatusUseCase(private val repository: MovieRepository) {
    operator fun invoke(movieId: Int): Flow<Boolean> = repository.observeIsFavorite(movieId)
}
