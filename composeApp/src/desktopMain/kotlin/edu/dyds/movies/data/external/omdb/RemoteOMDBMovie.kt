package edu.dyds.movies.data.external.omdb

import edu.dyds.movies.domain.entity.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteOMDBMovie(
    @SerialName("Title") val title: String? = null,
    @SerialName("Plot") val plot: String? = null,
    @SerialName("Released") val released: String? = null,
    @SerialName("Year") val year: String? = null,
    @SerialName("Poster") val poster: String? = null,
    @SerialName("Language") val language: String? = null,
    @SerialName("Metascore") val metaScore: String? = null,
    val imdbRating: String? = null,
    @SerialName("Response") val response: String? = null,
    @SerialName("Error") val error: String? = null
) {
    fun toDomainMovie() = Movie(
        id = title?.hashCode() ?: 0,
        title = title ?: "Unknown title",
        overview = plot ?: "No plot available",
        releaseDate = if (released?.isNotEmpty() == true && released != "N/A") released else (year ?: "Unknown"),
        poster = if (poster?.isNotEmpty() == true && poster != "N/A") poster else "",
        backdrop = if (poster?.isNotEmpty() == true && poster != "N/A") poster else "",
        originalTitle = title ?: "Unknown title",
        originalLanguage = language ?: "Unknown",
        popularity = imdbRating?.toDoubleOrNull() ?: 0.0,
        voteAverage = if (metaScore?.isNotEmpty() == true && metaScore != "N/A") metaScore.toDouble() else 0.0
    )

}