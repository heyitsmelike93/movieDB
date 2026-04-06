package org.hansgrohe.moviedb

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.hansgrohe.moviedb.presentation.movielist.MovieListScreen
import org.hansgrohe.moviedb.presentation.movielist.MovieListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    MaterialTheme {
        val viewModel: MovieListViewModel = koinViewModel()
        MovieListScreen(viewModel = viewModel)
    }
}