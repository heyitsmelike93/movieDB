package com.example.shared.domain.usecase

import com.example.shared.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class ObserveFavoriteMovieIdsUseCase(private val repository: MovieRepository) {
    operator fun invoke(): Flow<Set<Int>> = repository.observeFavoriteMovieIds()
}
