package edu.dyds.movies.data.external

import edu.dyds.movies.data.external.contracts.ExternalMovieDetailSource
import edu.dyds.movies.data.external.contracts.ExternalPopularMoviesSource

class ExternalMoviesSourceImpl(
    private val moviesListSource: ExternalPopularMoviesSource,
    private val moviesDetailSource: ExternalMovieDetailSource
) : ExternalMoviesSource {
    override suspend fun getPopularMovies() = moviesListSource.getPopularMovies()
    override suspend fun getMovieByTitle(title: String) = moviesDetailSource.getMovieByTitle(title)
}