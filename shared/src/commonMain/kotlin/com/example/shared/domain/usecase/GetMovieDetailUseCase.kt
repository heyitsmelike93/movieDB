package com.example.shared.domain.usecase

import com.example.shared.domain.model.MovieDetail
import com.example.shared.domain.repository.MovieRepository

class GetMovieDetailUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movieId: Int): Result<MovieDetail> =
        repository.getMovieDetail(movieId)
}
