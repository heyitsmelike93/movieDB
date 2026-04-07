package com.example.shared.data.local

import kotlinx.coroutines.flow.Flow

interface FavoriteMovieLocalDataSource {
    fun observeFavoriteMovieIds(): Flow<Set<Int>>
    suspend fun getFavoriteMovieIds(): Set<Int>
    suspend fun isFavorite(movieId: Int): Boolean
    suspend fun addFavorite(movieId: Int)
    suspend fun removeFavorite(movieId: Int)
}
