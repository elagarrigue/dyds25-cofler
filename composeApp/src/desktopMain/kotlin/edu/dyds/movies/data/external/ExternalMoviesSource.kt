package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

interface ExternalMoviesSource {
    suspend fun getPopularMovies(): List<Movie>
    suspend fun getMovieByTitle(title: String): Movie
}

internal class TMDBExternalMoviesImpl(private val tmdbHttpClient: HttpClient) : ExternalMoviesSource {
    override suspend fun getPopularMovies(): List<Movie> = getTMDBPopularMovies().results.map { it.toDomainMovie()}

    override suspend fun getMovieByTitle(title: String): Movie = getTMDBMovieDetails(title).apply{println(this)}.results.first().toDomainMovie()

    private suspend fun getTMDBPopularMovies(): RemoteResult =
        tmdbHttpClient.get("/3/discover/movie?sort_by=popularity.desc").body()

    private suspend fun getTMDBMovieDetails(title: String): RemoteResult =
        tmdbHttpClient.get("/3/search/movie?query=$title").body()
}

