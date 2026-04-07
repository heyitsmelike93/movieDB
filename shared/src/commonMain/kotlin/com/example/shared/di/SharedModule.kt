package com.example.shared.di

fun sharedModule(apiKey: String, isDebug: Boolean) = listOf(
    platformModule(),
    networkModule(apiKey, isDebug),
    dataModule,
    domainModule,
    presentationModule
)
