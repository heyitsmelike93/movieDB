package org.hansgrohe.moviedb

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.hansgrohe.moviedb.presentation.moviedetail.MovieDetailScreen
import org.hansgrohe.moviedb.presentation.moviedetail.MovieDetailViewModel
import org.hansgrohe.moviedb.presentation.movielist.MovieListScreen
import org.hansgrohe.moviedb.presentation.movielist.MovieListViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable object MovieListRoute
@Serializable data class MovieDetailRoute(val movieId: Int)

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = MovieListRoute) {
            composable<MovieListRoute> {
                val viewModel: MovieListViewModel = koinViewModel()
                MovieListScreen(
                    viewModel = viewModel,
                    onMovieClick = { movieId -> navController.navigate(MovieDetailRoute(movieId)) }
                )
            }
            composable<MovieDetailRoute> { backStackEntry ->
                val route: MovieDetailRoute = backStackEntry.toRoute()
                val viewModel: MovieDetailViewModel = koinViewModel(
                    parameters = { parametersOf(route.movieId) }
                )
                MovieDetailScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}