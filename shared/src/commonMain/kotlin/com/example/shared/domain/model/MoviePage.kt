package com.example.shared.domain.model

data class MoviePage(
    val movies: List<Movie>,
    val page: Int,
    val totalPages: Int
)
