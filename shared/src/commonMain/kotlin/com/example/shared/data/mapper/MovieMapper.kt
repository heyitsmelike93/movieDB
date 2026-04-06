package com.example.shared.data.mapper

import com.example.shared.data.remote.dto.GenreDto
import com.example.shared.data.remote.dto.MovieDetailDto
import com.example.shared.data.remote.dto.MovieDto
import com.example.shared.domain.model.Genre
import com.example.shared.domain.model.Movie
import com.example.shared.domain.model.MovieDetail

internal fun MovieDto.toDomain() = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    voteAverage = voteAverage,
    releaseDate = releaseDate
)

internal fun MovieDetailDto.toDomain() = MovieDetail(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    voteAverage = voteAverage,
    voteCount = voteCount,
    releaseDate = releaseDate,
    runtime = runtime,
    genres = genres.map { it.toDomain() },
    tagline = tagline,
    status = status
)

internal fun GenreDto.toDomain() = Genre(id = id, name = name)
