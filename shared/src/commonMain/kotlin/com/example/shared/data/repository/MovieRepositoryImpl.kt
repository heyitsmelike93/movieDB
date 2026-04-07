package com.example.shared.data.repository

import com.example.shared.data.MovieApi
import com.example.shared.data.local.FavoriteMovieLocalDataSource
import com.example.shared.data.mapper.toDomain
import com.example.shared.domain.model.MovieDetail
import com.example.shared.domain.model.MoviePage
import com.example.shared.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MovieRepositoryImpl(
    private val api: MovieApi,
    private val favoriteLocalDataSource: FavoriteMovieLocalDataSource
) : MovieRepository {
    override suspend fun getPopularMovies(page: Int): Result<MoviePage> = runCatching {
        val favoriteIds = favoriteLocalDataSource.getFavoriteMovieIds()
        val response = api.getPopularMovies(page)
        MoviePage(
            movies = response.results.map { movieDto ->
                movieDto.toDomain().copy(isFavorite = movieDto.id in favoriteIds)
            },
            page = response.page,
            totalPages = response.totalPages
        )
    }

    override suspend fun getMovieDetail(movieId: Int): Result<MovieDetail> = runCatching {
        api.getMovieDetail(movieId)
            .toDomain()
            .copy(isFavorite = favoriteLocalDataSource.isFavorite(movieId))
    }

    override fun observeFavoriteMovieIds(): Flow<Set<Int>> =
        favoriteLocalDataSource.observeFavoriteMovieIds()

    override fun observeIsFavorite(movieId: Int): Flow<Boolean> =
        observeFavoriteMovieIds().map { movieId in it }

    override suspend fun toggleFavorite(movieId: Int) {
        if (favoriteLocalDataSource.isFavorite(movieId)) {
            favoriteLocalDataSource.removeFavorite(movieId)
        } else {
            favoriteLocalDataSource.addFavorite(movieId)
        }
    }
}
