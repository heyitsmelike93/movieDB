package com.example.shared.domain.usecase

import com.example.shared.domain.repository.MovieRepository

class ToggleFavoriteMovieUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movieId: Int) = repository.toggleFavorite(movieId)
}
