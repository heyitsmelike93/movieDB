package com.example.shared.di

import com.example.shared.data.MovieApi
import com.example.shared.data.remote.KtorMovieApi
import com.example.shared.data.remote.createHttpClient
import com.example.shared.data.repository.MovieRepositoryImpl
import com.example.shared.domain.repository.MovieRepository
import org.koin.dsl.module

val sharedModule = module {
    single { createHttpClient() }
    single<MovieApi> { KtorMovieApi(get()) }
    single<MovieRepository> { MovieRepositoryImpl(get()) }
}
