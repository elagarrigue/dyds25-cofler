package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie

interface LocalMoviesSource {
    fun getCachedMovies(): List<Movie>
    fun cacheMovies(movies: List<Movie>)
}

internal class LocalMoviesImpl : LocalMoviesSource {
    private val cacheMovies: MutableList<Movie> = mutableListOf()

    override fun getCachedMovies() = cacheMovies.toList()

    override fun cacheMovies(movies: List<Movie>) {
        cacheMovies.clear()
        cacheMovies.addAll(movies)
    }
}

