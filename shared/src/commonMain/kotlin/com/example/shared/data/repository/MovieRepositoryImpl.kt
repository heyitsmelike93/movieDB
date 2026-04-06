package com.example.shared.data.repository

import com.example.shared.data.MovieApi
import com.example.shared.data.remote.dto.MovieDto
import com.example.shared.domain.model.Movie
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

    private fun MovieDto.toDomain() = Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        voteAverage = voteAverage,
        releaseDate = releaseDate
    )
}
