package com.example.shared.di

import com.example.shared.data.MovieApi
import com.example.shared.data.remote.KtorMovieApi
import com.example.shared.data.remote.createHttpClient
import com.example.shared.data.repository.MovieRepositoryImpl
import com.example.shared.domain.repository.MovieRepository
import com.example.shared.domain.usecase.GetMovieDetailUseCase
import com.example.shared.domain.usecase.GetPopularMoviesUseCase
import org.koin.dsl.module

fun sharedModule(apiKey: String) = module {
    single { createHttpClient(apiKey) }
    single<MovieApi> { KtorMovieApi(get()) }
    single<MovieRepository> { MovieRepositoryImpl(get()) }
    factory { GetPopularMoviesUseCase(get()) }
    factory { GetMovieDetailUseCase(get()) }
}
