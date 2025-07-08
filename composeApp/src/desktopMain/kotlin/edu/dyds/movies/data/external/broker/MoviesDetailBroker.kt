package edu.dyds.movies.data.external.broker

import edu.dyds.movies.data.external.contracts.ExternalMovieDetailSource
import edu.dyds.movies.domain.entity.Movie

class MoviesDetailBroker(
    private val tmdbSource: ExternalMovieDetailSource,
    private val omdbSource: ExternalMovieDetailSource
) : ExternalMovieDetailSource {

    override suspend fun getMovieByTitle(title: String): Movie? {
        val tmdb = safeFetch { tmdbSource.getMovieByTitle(title) }
        val omdb = safeFetch { omdbSource.getMovieByTitle(title) }

        return combineSources(tmdb, omdb)
    }

    private suspend fun safeFetch(fetch: suspend () -> Movie?): Movie? {
        return try {
            fetch()
        } catch (e: Exception) {
            null
        }
    }

    private fun combineSources(tmdb: Movie?, omdb: Movie?): Movie? {
        return when {
            tmdb != null && omdb != null -> buildMovie(tmdb, omdb)
            tmdb != null -> tmdb.copy(overview = "TMDB: ${tmdb.overview}")
            omdb != null -> omdb.copy(overview = "OMDB: ${omdb.overview}")
            else -> null
        }
    }

    private fun buildMovie(
        tmdbMovie: Movie,
        omdbMovie: Movie
    ) = Movie(
        id = tmdbMovie.id,
        title = tmdbMovie.title,
        overview = "TMDB: ${tmdbMovie.overview}\n\nOMDB: ${omdbMovie.overview}",
        releaseDate = tmdbMovie.releaseDate,
        poster = tmdbMovie.poster,
        backdrop = tmdbMovie.backdrop,
        originalTitle = tmdbMovie.originalTitle,
        originalLanguage = tmdbMovie.originalLanguage,
        popularity = (tmdbMovie.popularity + omdbMovie.popularity) / 2.0,
        voteAverage = (tmdbMovie.voteAverage + omdbMovie.voteAverage) / 2.0
    )
}