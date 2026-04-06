package com.example.shared.data

import com.example.shared.data.remote.dto.MovieListResponseDto

interface MovieApi {
    suspend fun getPopularMovies(page: Int): MovieListResponseDto
}