package com.example.shared.data.remote

import com.example.shared.data.MovieApi
import com.example.shared.data.remote.dto.MovieListResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class KtorMovieApi(private val client: HttpClient) : MovieApi {
    override suspend fun getPopularMovies(page: Int): MovieListResponseDto {
        return client.get("movie/popular") {
            parameter("page", page)
        }.body()
    }
}
