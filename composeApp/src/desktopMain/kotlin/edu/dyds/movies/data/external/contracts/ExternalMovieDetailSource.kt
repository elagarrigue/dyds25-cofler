package edu.dyds.movies.data.external.contracts

import edu.dyds.movies.domain.entity.Movie

interface ExternalMovieDetailSource{
    suspend fun getMovieByTitle(title: String): Movie?
}