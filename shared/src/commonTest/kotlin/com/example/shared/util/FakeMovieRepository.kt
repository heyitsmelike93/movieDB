package com.example.shared.util

import com.example.shared.domain.model.Movie
import com.example.shared.domain.model.MovieDetail
import com.example.shared.domain.model.MoviePage
import com.example.shared.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeMovieRepository(
    var movieDetailResult: Result<MovieDetail> = Result.success(fakeMovieDetail()),
    var popularMoviesResult: Result<MoviePage> = Result.success(fakeMoviePage()),
    var toggleFavoriteError: Throwable? = null
) : MovieRepository {

    private val favoriteIds = MutableStateFlow<Set<Int>>(emptySet())

    override suspend fun getPopularMovies(page: Int) = popularMoviesResult

    override suspend fun getMovieDetail(movieId: Int) = movieDetailResult

    override fun observeFavoriteMovieIds(): Flow<Set<Int>> = favoriteIds

    override fun observeIsFavorite(movieId: Int): Flow<Boolean> =
        favoriteIds.map { movieId in it }

    override suspend fun toggleFavorite(movieId: Int) {
        toggleFavoriteError?.let { throw it }
        favoriteIds.value = favoriteIds.value.toMutableSet().apply {
            if (movieId in this) remove(movieId) else add(movieId)
        }
    }
}

fun fakeMovieDetail(id: Int = 1) = MovieDetail(
    id = id,
    title = "Test Movie",
    overview = "Test overview",
    posterPath = null,
    backdropPath = null,
    voteAverage = 7.5,
    voteCount = 100,
    releaseDate = "2024-01-01",
    runtime = 120,
    genres = emptyList(),
    tagline = null,
    status = "Released"
)

fun fakeMovie(id: Int = 1) = Movie(
    id = id,
    title = "Test Movie $id",
    overview = "Overview",
    posterPath = null,
    voteAverage = 7.0,
    releaseDate = "2024-01-01"
)

fun fakeMoviePage(page: Int = 1, totalPages: Int = 3) = MoviePage(
    movies = listOf(fakeMovie(1), fakeMovie(2)),
    page = page,
    totalPages = totalPages
)
