package com.example.shared.domain.repository

import com.example.shared.domain.model.MovieDetail
import com.example.shared.domain.model.MoviePage
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): Result<MoviePage>
    suspend fun getMovieDetail(movieId: Int): Result<MovieDetail>
    fun observeFavoriteMovieIds(): Flow<Set<Int>>
    fun observeIsFavorite(movieId: Int): Flow<Boolean>
    suspend fun toggleFavorite(movieId: Int)
}
