package edu.dyds.movies.data.external
import edu.dyds.movies.domain.entity.Movie

interface ExternalPopularMoviesSource {
    suspend fun getPopularMovies(): List<Movie>
}

interface ExternalMovieDetailSource {
    suspend fun getMovieByTitle(title: String): Movie?
}

interface ExternalMoviesSource : ExternalPopularMoviesSource, ExternalMovieDetailSource

