package edu.dyds.movies.data

import edu.dyds.movies.data.external.ExternalMoviesSource
import edu.dyds.movies.data.local.LocalMoviesSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class MoviesRepositoryImpl(private val localMovies: LocalMoviesSource, private val externalMovies: ExternalMoviesSource): MoviesRepository {
    override suspend fun getPopularMovies(): List<Movie> {
        val cachedMovies = localMovies.getCachedMovies()

        return cachedMovies.ifEmpty {
            try {
                externalMovies.getPopularMovies().apply {
                    localMovies.cacheMovies(this)
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getMovieDetails(id: Int) =
        try {
            externalMovies.getMovieDetails(id)
        } catch (e: Exception) {
            null
        }
}