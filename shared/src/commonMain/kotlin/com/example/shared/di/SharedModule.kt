package com.example.shared.di

fun sharedModule(apiKey: String, isDebug: Boolean) = listOf(
    networkModule(apiKey, isDebug),
    dataModule,
    domainModule
)
