package org.hansgrohe.moviedb

import android.app.Application
import com.example.shared.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MovieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MovieApplication)
            modules(sharedModule(BuildConfig.TMDB_API_KEY, BuildConfig.DEBUG))
        }
    }
}
