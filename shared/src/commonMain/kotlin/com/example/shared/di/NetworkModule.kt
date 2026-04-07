package com.example.shared.di

import com.example.shared.data.MovieApi
import com.example.shared.data.remote.KtorMovieApi
import com.example.shared.data.remote.createHttpClient
import org.koin.dsl.module

internal fun networkModule(apiKey: String, isDebug: Boolean) = module {
    single { createHttpClient(apiKey, isDebug) }
    single<MovieApi> { KtorMovieApi(get()) }
}
