package com.example.shared.domain.model

data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val releaseDate: String,
    val runtime: Int?,
    val genres: List<Genre>,
    val tagline: String?,
    val status: String
) {
    val posterUrl: String?
        get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }

    val backdropUrl: String?
        get() = backdropPath?.let { "https://image.tmdb.org/t/p/w1280$it" }

    val formattedRuntime: String?
        get() = runtime?.let { "${it / 60}h ${it % 60}m" }

    val formattedRating: String
        get() = "${(voteAverage * 10).toInt() / 10.0}"
}
