package com.example.shared.di

import com.example.shared.presentation.moviedetail.MovieDetailViewModel
import com.example.shared.presentation.movielist.MovieListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val presentationModule = module {
    viewModel { MovieListViewModel(get(), get(), get()) }
    viewModel { params -> MovieDetailViewModel(get(), get(), get(), params.get()) }
}
