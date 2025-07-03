package edu.dyds.movies.data.external.tmdb

import edu.dyds.movies.data.external.RemoteResult
import edu.dyds.movies.domain.entity.Movie

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

interface ExternalPopularMoviesSource {
    suspend fun getPopularMovies(): List<Movie>
}

interface ExternalMovieDetailSource{
    suspend fun getMovieByTitle(title: String): Movie
}


public class TMDBExternalMoviesSource(private val tmdbHttpClient: HttpClient) : ExternalPopularMoviesSource, ExternalMovieDetailSource {
    override suspend fun getPopularMovies(): List<Movie> = getTMDBPopularMovies().results.map { it.toDomainMovie()}

    override suspend fun getMovieByTitle(title: String): Movie = getTMDBMovieDetails(title).apply{println(this)}.results.first().toDomainMovie()

    private suspend fun getTMDBPopularMovies(): RemoteResult =
        tmdbHttpClient.get("/3/discover/movie?sort_by=popularity.desc").body()

    private suspend fun getTMDBMovieDetails(title: String): RemoteResult =
        tmdbHttpClient.get("/3/search/movie?query=$title").body()
}

internal class OMDBExternalMoviesSource(private val omdbHttpClient: HttpClient): ExternalMovieDetailSource{
    override suspend fun getMovieByTitle(title: String): Movie = getOMDBMovieDetails(title).apply{println(this)}.results.first().toDomainMovie()

     private suspend fun getOMDBMovieDetails(title:String): RemoteResult =
        omdbHttpClient.get("/3/discover/movie?sort_by=popularity.desc").body()
}

