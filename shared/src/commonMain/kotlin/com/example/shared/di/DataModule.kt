package com.example.shared.di

import com.example.shared.data.local.DatabaseDriverFactory
import com.example.shared.data.local.FavoriteMovieLocalDataSource
import com.example.shared.data.local.FavoriteMovieLocalDataSourceImpl
import com.example.shared.data.repository.MovieRepositoryImpl
import com.example.shared.db.FavoriteMovie
import com.example.shared.domain.repository.MovieRepository
import org.koin.dsl.module

internal val dataModule = module {
    single { FavoriteMovie(get<DatabaseDriverFactory>().createDriver()) }
    single<FavoriteMovieLocalDataSource> { FavoriteMovieLocalDataSourceImpl(get()) }
    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }
}
