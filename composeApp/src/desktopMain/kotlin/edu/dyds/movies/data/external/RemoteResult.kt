package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p"

@Serializable
data class RemoteResult(
    val page: Int,
    val results: List<RemoteMovie>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)

@Serializable
data class RemoteMovie(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("release_date") val releaseDate: String?,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("original_language") val originalLanguage: String,
    val popularity: Double?,
    @SerialName("vote_average") val voteAverage: Double?,

    ) {
    fun toDomainMovie(): Movie {
        return Movie(
            id = id,
            title = title,
            overview = overview,
            releaseDate = releaseDate?: "",
            poster = "${TMDB_IMAGE_BASE_URL}/w185$posterPath",
            backdrop = backdropPath.let { "${TMDB_IMAGE_BASE_URL}/w780$it" },
            originalTitle = originalTitle,
            originalLanguage = originalLanguage,
            popularity = popularity ?: 0.0,
            voteAverage = voteAverage ?: 0.0
        )
    }
}