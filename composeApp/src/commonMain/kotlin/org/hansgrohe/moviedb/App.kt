package org.hansgrohe.moviedb

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.hansgrohe.moviedb.navigation.AppNavigation

@Composable
fun App() {
    MaterialTheme {
        AppNavigation()
    }
}