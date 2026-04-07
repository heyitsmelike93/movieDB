package com.example.shared.di

import com.example.shared.data.repository.MovieRepositoryImpl
import com.example.shared.domain.repository.MovieRepository
import org.koin.dsl.module

internal val dataModule = module {
    single<MovieRepository> { MovieRepositoryImpl(get()) }
}
