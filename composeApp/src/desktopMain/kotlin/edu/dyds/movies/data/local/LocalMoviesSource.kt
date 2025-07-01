package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie

interface LocalMoviesSource {
    fun getMovies(): List<Movie>
    fun setMovies(movies: List<Movie>)
}

internal class LocalMoviesImpl : LocalMoviesSource {
    private val cacheMovies: MutableList<Movie> = mutableListOf()

    override fun getMovies() = cacheMovies.toList()

    override fun setMovies(movies: List<Movie>) {
        cacheMovies.clear()
        cacheMovies.addAll(movies)
    }
}

