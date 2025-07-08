package edu.dyds.movies.data.external

import edu.dyds.movies.data.external.contracts.ExternalMovieDetailSource
import edu.dyds.movies.data.external.contracts.ExternalPopularMoviesSource

class ExternalMoviesSourceImpl(
    private val moviesListSource: ExternalPopularMoviesSource,
    private val moviesDetailSource: ExternalMovieDetailSource
) : ExternalMoviesSource,
    ExternalPopularMoviesSource by moviesListSource,
    ExternalMovieDetailSource by moviesDetailSource
