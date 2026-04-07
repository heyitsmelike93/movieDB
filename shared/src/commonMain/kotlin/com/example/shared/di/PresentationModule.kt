package com.example.shared.di

import com.example.shared.presentation.moviedetail.MovieDetailViewModel
import com.example.shared.presentation.movielist.MovieListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val presentationModule = module {
    viewModelOf(::MovieListViewModel)
    viewModel { params -> MovieDetailViewModel(get(), params.get()) }
}
