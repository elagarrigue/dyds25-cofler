package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

interface ExternalMoviesSource {
    suspend fun getPopularMovies(): List<Movie>
    suspend fun getMovieDetails(id: Int): Movie
}

internal class ExternalMoviesImpl(private val tmdbHttpClient: HttpClient) : ExternalMoviesSource {
    override suspend fun getPopularMovies(): List<Movie> = getTMDBPopularMovies().results.map { it.toDomainMovie()}

    override suspend fun getMovieDetails(id: Int): Movie = getTMDBMovieDetails(id).toDomainMovie()

    private suspend fun getTMDBPopularMovies(): RemoteResult =
        tmdbHttpClient.get("/3/discover/movie?sort_by=popularity.desc").body()

    private suspend fun getTMDBMovieDetails(id: Int): RemoteMovie =
        tmdbHttpClient.get("/3/movie/$id").body()
}

