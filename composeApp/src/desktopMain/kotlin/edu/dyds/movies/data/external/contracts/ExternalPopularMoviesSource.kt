package edu.dyds.movies.data.external.contracts

import edu.dyds.movies.domain.entity.Movie

interface ExternalPopularMoviesSource {
    suspend fun getPopularMovies(): List<Movie>
}