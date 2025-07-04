package edu.dyds.movies.data.external

import edu.dyds.movies.data.external.contracts.ExternalMovieDetailSource
import edu.dyds.movies.data.external.contracts.ExternalPopularMoviesSource
import edu.dyds.movies.domain.entity.Movie

class ExternalMoviesSourceImpl  (
    private val moviesListSource: ExternalPopularMoviesSource,
    private val moviesDetailSource: ExternalMovieDetailSource
) : ExternalMoviesSource {
    override suspend fun getPopularMovies(): List<Movie> {
        return moviesListSource.getPopularMovies()
    }

    override suspend fun getMovieByTitle(title: String): Movie? {
        return moviesDetailSource.getMovieByTitle(title)
    }
}