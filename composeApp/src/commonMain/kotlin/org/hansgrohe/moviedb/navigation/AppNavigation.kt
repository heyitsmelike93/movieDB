package org.hansgrohe.moviedb.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.hansgrohe.moviedb.presentation.moviedetail.MovieDetailScreen
import org.hansgrohe.moviedb.presentation.movielist.MovieListScreen

@Serializable object MovieListRoute
@Serializable data class MovieDetailRoute(val movieId: Int)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MovieListRoute) {
        composable<MovieListRoute> {
            MovieListScreen(
                onMovieClick = { movieId -> navController.navigate(MovieDetailRoute(movieId)) }
            )
        }
        composable<MovieDetailRoute> { backStackEntry ->
            val route: MovieDetailRoute = backStackEntry.toRoute()
            MovieDetailScreen(
                movieId = route.movieId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
