package edu.dyds.movies.data

import edu.dyds.movies.data.external.tmdb.TMDBExternalMoviesSource
import edu.dyds.movies.data.local.LocalMoviesSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class MoviesRepositoryImpl(private val localMoviesSource: LocalMoviesSource, private val externalMoviesSource: TMDBExternalMoviesSource): MoviesRepository {
    override suspend fun getPopularMovies(): List<Movie> {
        val localMovies = localMoviesSource.getMovies()

        return localMovies.ifEmpty {
            try {
                externalMoviesSource.getPopularMovies().apply {
                    localMoviesSource.setMovies(this)
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getMovieByTitle(title: String) =
        try {
            externalMoviesSource.getMovieByTitle(title)
        } catch (e: Exception) {
            null
        }
}