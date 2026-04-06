package com.example.shared.domain.usecase

import com.example.shared.domain.model.MoviePage
import com.example.shared.domain.repository.MovieRepository

class GetPopularMoviesUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(page: Int): Result<MoviePage> =
        repository.getPopularMovies(page)
}
