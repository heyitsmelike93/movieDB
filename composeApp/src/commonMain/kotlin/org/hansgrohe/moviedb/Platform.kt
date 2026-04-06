package org.hansgrohe.moviedb

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform