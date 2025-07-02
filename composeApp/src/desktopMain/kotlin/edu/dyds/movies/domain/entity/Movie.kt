package edu.dyds.movies.domain.entity

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val poster: String,
    val backdrop: String?,
    val originalTitle: String,
    val originalLanguage: String,
    val popularity: Double,
    val voteAverage: Double
)

data class QualifiedMovie(val movie: Movie, val isGoodMovie: Boolean)

private fun buildMovie(
    tmdbMovie: Movie,
    omdbMovie: Movie
): Movie {
    return Movie(
        id = tmdbMovie.id,
        title = tmdbMovie.title,
        overview = "TMDB: ${tmdbMovie.overview}\n\nOMDB: ${omdbMovie.overview}",
        releaseDate = tmdbMovie.releaseDate,
        poster = tmdbMovie.poster,
        backdrop = tmdbMovie.backdrop,
        originalTitle = tmdbMovie.originalTitle,
        originalLanguage = tmdbMovie.originalLanguage,
        popularity = (tmdbMovie.popularity + omdbMovie.popularity) / 2.0,
        voteAverage = (tmdbMovie.voteAverage + omdbMovie.voteAverage) / 2.0
    )
}

