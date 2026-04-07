package com.example.shared.presentation.moviedetail

import com.example.shared.domain.usecase.GetMovieDetailUseCase
import com.example.shared.domain.usecase.ObserveMovieFavoriteStatusUseCase
import com.example.shared.domain.usecase.ToggleFavoriteMovieUseCase
import com.example.shared.util.FakeMovieRepository
import com.example.shared.util.fakeMovieDetail
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
class MovieDetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(repo: FakeMovieRepository, movieId: Int = 1) =
        MovieDetailViewModel(
            getMovieDetail = GetMovieDetailUseCase(repo),
            observeMovieFavoriteStatus = ObserveMovieFavoriteStatusUseCase(repo),
            toggleFavoriteUseCase = ToggleFavoriteMovieUseCase(repo),
            movieId = movieId
        )

    @Test
    fun `when repository succeeds, uiState becomes Success with correct movie`() = runTest {
        val detail = fakeMovieDetail(id = 42)
        val repo = FakeMovieRepository(movieDetailResult = Result.success(detail))
        val viewModel = createViewModel(repo, movieId = 42)

        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is MovieDetailUiState.Success)
        assertEquals(42, state.movieDetail.id)
        assertEquals("Test Movie", state.movieDetail.title)
    }

    @Test
    fun `when repository fails, uiState becomes Error with message`() = runTest {
        val repo = FakeMovieRepository(
            movieDetailResult = Result.failure(Exception("Network error"))
        )
        val viewModel = createViewModel(repo)

        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is MovieDetailUiState.Error)
        assertEquals("Network error", state.message)
    }

    @Test
    fun `when toggleFavorite fails, toastMessage is emitted`() = runTest {
        val repo = FakeMovieRepository(
            toggleFavoriteError = RuntimeException("DB write failed")
        )
        val viewModel = createViewModel(repo)

        val toasts = mutableListOf<String>()
        backgroundScope.launch(testDispatcher) { viewModel.toastMessage.collect { toasts.add(it) } }

        viewModel.toggleFavorite()
        advanceUntilIdle()

        assertEquals(1, toasts.size)
        assertEquals("Failed to update favorite", toasts.first())
    }

    @Test
    fun `when favorite status changes, uiState reflects updated isFavorite`() = runTest {
        val repo = FakeMovieRepository()
        val viewModel = createViewModel(repo, movieId = 1)

        backgroundScope.launch(testDispatcher) { viewModel.uiState.collect {} }
        advanceUntilIdle()

        val before = viewModel.uiState.value
        assertTrue(before is MovieDetailUiState.Success)
        assertEquals(false, before.movieDetail.isFavorite)

        repo.toggleFavorite(movieId = 1) // DB'ye doğrudan yaz
        advanceUntilIdle()

        val after = viewModel.uiState.value
        assertTrue(after is MovieDetailUiState.Success)
        assertEquals(true, after.movieDetail.isFavorite)
    }
}
