package edu.dyds.movies.data.external.tmdb

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.data.external.contracts.ExternalMovieDetailSource
import edu.dyds.movies.data.external.contracts.ExternalPopularMoviesSource
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class TMDBExternalMoviesSource(private val tmdbHttpClient: HttpClient) : ExternalPopularMoviesSource, ExternalMovieDetailSource {
    override suspend fun getPopularMovies(): List<Movie> = getTMDBPopularMovies().results.map { it.toDomainMovie()}

    override suspend fun getMovieByTitle(title: String): Movie = getTMDBMovieDetails(title).results.first().toDomainMovie()

    private suspend fun getTMDBPopularMovies(): RemoteResult =
        tmdbHttpClient.get("/3/discover/movie?sort_by=popularity.desc").body()

    private suspend fun getTMDBMovieDetails(title: String): RemoteResult =
        tmdbHttpClient.get("/3/search/movie?query=$title").body()
}