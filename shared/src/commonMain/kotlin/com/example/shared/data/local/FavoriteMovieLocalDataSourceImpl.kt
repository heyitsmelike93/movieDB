package com.example.shared.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.shared.db.FavoriteMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class FavoriteMovieLocalDataSourceImpl(
    database: FavoriteMovie
) : FavoriteMovieLocalDataSource {

    private val queries = database.favoriteMovieQueries

    override fun observeFavoriteMovieIds(): Flow<Set<Int>> =
        queries.selectAllFavoriteMovieIds()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toInt() }.toSet() }

    override suspend fun getFavoriteMovieIds(): Set<Int> =
        queries.selectAllFavoriteMovieIds().executeAsList().map { it.toInt() }.toSet()

    override suspend fun isFavorite(movieId: Int): Boolean =
        queries.selectFavoriteMovieById(movieId.toLong()).executeAsOneOrNull() != null

    override suspend fun addFavorite(movieId: Int) {
        queries.insertFavoriteMovie(movieId.toLong())
    }

    override suspend fun removeFavorite(movieId: Int) {
        queries.deleteFavoriteMovieById(movieId.toLong())
    }
}
