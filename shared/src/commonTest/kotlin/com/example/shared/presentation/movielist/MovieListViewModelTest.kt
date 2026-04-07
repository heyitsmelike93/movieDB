package com.example.shared.presentation.movielist

import com.example.shared.domain.usecase.GetPopularMoviesUseCase
import com.example.shared.domain.usecase.ObserveFavoriteMovieIdsUseCase
import com.example.shared.domain.usecase.ToggleFavoriteMovieUseCase
import com.example.shared.util.FakeMovieRepository
import com.example.shared.util.fakeMovie
import com.example.shared.util.fakeMoviePage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(repo: FakeMovieRepository) = MovieListViewModel(
        getPopularMovies = GetPopularMoviesUseCase(repo),
        observeFavoriteMovieIds = ObserveFavoriteMovieIdsUseCase(repo),
        toggleFavoriteUseCase = ToggleFavoriteMovieUseCase(repo)
    )

    @Test
    fun `when repository succeeds, uiState becomes Success with movies`() = runTest {
        val page1 = fakeMoviePage(page = 1, totalPages = 3)
        val repo = FakeMovieRepository(popularMoviesResult = Result.success(page1))
        val viewModel = createViewModel(repo)

        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is MovieListUiState.Success)
        assertEquals(2, state.movies.size)
        assertEquals(1, state.currentPage)
        assertEquals(3, state.totalPages)
    }

    @Test
    fun `loadMore appends next page movies`() = runTest {
        val page1 = fakeMoviePage(page = 1, totalPages = 2)
        val page2 = fakeMoviePage(page = 2, totalPages = 2).copy(
            movies = listOf(fakeMovie(3), fakeMovie(4))
        )
        val repo = FakeMovieRepository(popularMoviesResult = Result.success(page1))
        val viewModel = createViewModel(repo)

        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }
        advanceUntilIdle()

        repo.popularMoviesResult = Result.success(page2)
        viewModel.loadMore()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is MovieListUiState.Success)
        assertEquals(4, state.movies.size)
        assertEquals(2, state.currentPage)
    }

    @Test
    fun `when toggleFavorite fails, toastMessage is emitted`() = runTest {
        val repo = FakeMovieRepository(
            toggleFavoriteError = RuntimeException("DB write failed")
        )
        val viewModel = createViewModel(repo)

        val toasts = mutableListOf<String>()
        backgroundScope.launch(testDispatcher) { viewModel.toastMessage.collect { toasts.add(it) } }

        viewModel.toggleFavorite(movieId = 1)
        advanceUntilIdle()

        assertEquals(1, toasts.size)
        assertEquals("Failed to update favorite", toasts.first())
    }
}
