package org.hansgrohe.moviedb.di

import org.hansgrohe.moviedb.presentation.moviedetail.MovieDetailViewModel
import org.hansgrohe.moviedb.presentation.movielist.MovieListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MovieListViewModel)
    viewModel { params -> MovieDetailViewModel(get(), params.get()) }
}
