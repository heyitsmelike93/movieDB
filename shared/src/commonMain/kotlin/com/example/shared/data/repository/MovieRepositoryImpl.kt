package com.example.shared.data.repository

import com.example.shared.data.MovieApi
import com.example.shared.data.mapper.toDomain
import com.example.shared.domain.model.MovieDetail
import com.example.shared.domain.model.MoviePage
import com.example.shared.domain.repository.MovieRepository

internal class MovieRepositoryImpl(private val api: MovieApi) : MovieRepository {
    override suspend fun getPopularMovies(page: Int): Result<MoviePage> = runCatching {
        val response = api.getPopularMovies(page)
        MoviePage(
            movies = response.results.map { it.toDomain() },
            page = response.page,
            totalPages = response.totalPages
        )
    }

    override suspend fun getMovieDetail(movieId: Int): Result<MovieDetail> = runCatching {
        api.getMovieDetail(movieId).toDomain()
    }
}
