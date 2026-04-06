package com.example.shared.domain.repository

import com.example.shared.domain.model.MoviePage

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): Result<MoviePage>
}
