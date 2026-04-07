package com.example.shared.di

import com.example.shared.data.local.DatabaseDriverFactory
import org.koin.dsl.module

internal actual fun platformModule() = module {
    single { DatabaseDriverFactory() }
}
