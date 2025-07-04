package edu.dyds.movies.data.external.omdb

import edu.dyds.movies.data.external.contracts.ExternalMovieDetailSource
import edu.dyds.movies.domain.entity.Movie
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class OMDBExternalMoviesSource(private val omdbHttpClient: HttpClient): ExternalMovieDetailSource {
    override suspend fun getMovieByTitle(title: String): Movie = getOMDBMovieDetails(title).toDomainMovie()

    private suspend fun getOMDBMovieDetails(title:String): RemoteOMDBMovie =
        omdbHttpClient.get("/?t=$title").body()
}
