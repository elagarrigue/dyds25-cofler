package edu.dyds.movies.data.external.broker

import edu.dyds.movies.data.external.contracts.ExternalMovieDetailSource
import edu.dyds.movies.domain.entity.Movie
import org.junit.Assert
import org.junit.Test

class MoviesDetailBrokerTest {

    class ExternalMoviesSourceFake(private val movie: Movie?) : ExternalMovieDetailSource {
        override suspend fun getMovieByTitle(title: String): Movie? =
            if (movie?.title == title) movie else null
    }

    private val tmdbMovie = Movie(
        id = 1,
        title = "Avatar",
        overview = "TMDB Overview",
        releaseDate = "2015-03-12",
        poster = "avatar_tmdb_poster.jpg",
        backdrop = "avatar_tmdb_backdrop.jpg",
        originalTitle = "Avatar",
        originalLanguage = "en",
        popularity = 85.5,
        voteAverage = 8.7
    )

    private val omdbMovie = Movie(
        id = 1,
        title = "Avatar",
        overview = "OMDB Overview",
        releaseDate = "2012-04-28",
        poster = "avatar_omdb_poster.jpg",
        backdrop = "avatar_omdb_backdrop.jpg",
        originalTitle = "Avatar",
        originalLanguage = "en",
        popularity = 95.2,
        voteAverage = 9.3
    )

    @Test
    fun `when both sources provide data should merge movie information correctly`() {
        val broker = MoviesDetailBroker(
            tmdbSource = ExternalMoviesSourceFake(tmdbMovie),
            omdbSource = ExternalMoviesSourceFake(omdbMovie)
        )

        val result = kotlinx.coroutines.runBlocking {
            broker.getMovieByTitle("Avatar")
        }

        Assert.assertEquals("Avatar", result?.title)
        Assert.assertEquals(
            "TMDB: ${tmdbMovie.overview}\n\nOMDB: ${omdbMovie.overview}",
            result?.overview
        )
        Assert.assertEquals(90.35, result?.popularity)
        Assert.assertEquals(9.0, result?.voteAverage)
    }

    @Test
    fun `when OMDB source fails should return TMDB data only`() {
        val broker = MoviesDetailBroker(
            tmdbSource = ExternalMoviesSourceFake(tmdbMovie),
            omdbSource = ExternalMoviesSourceFake(null)
        )

        val result = kotlinx.coroutines.runBlocking {
            broker.getMovieByTitle("Avatar")
        }

        Assert.assertEquals("Avatar", result?.title)
        Assert.assertEquals("TMDB: ${tmdbMovie.overview}", result?.overview)
    }

    @Test
    fun `when TMDB source fails should return OMDB data only`() {
        val broker = MoviesDetailBroker(
            tmdbSource = ExternalMoviesSourceFake(null),
            omdbSource = ExternalMoviesSourceFake(omdbMovie)
        )

        val result = kotlinx.coroutines.runBlocking {
            broker.getMovieByTitle("Avatar")
        }

        Assert.assertEquals("Avatar", result?.title)
        Assert.assertEquals("OMDB: ${omdbMovie.overview}", result?.overview)
    }

    @Test
    fun `should return null when both sources return null`() {
        val broker = MoviesDetailBroker(
            tmdbSource = ExternalMoviesSourceFake(null),
            omdbSource = ExternalMoviesSourceFake(null)
        )

        val result = kotlinx.coroutines.runBlocking {
            broker.getMovieByTitle("Avatar")
        }

        Assert.assertNull(result)
    }

    class ExceptionThrowingSource(private val throwOnCall: Boolean, private val movie: Movie? = null) : ExternalMovieDetailSource {
        override suspend fun getMovieByTitle(title: String): Movie? {
            if (throwOnCall) throw RuntimeException("Simulated failure")
            return movie
        }
    }

    @Test
    fun `when TMDB throws exception should return OMDB data only`() {
        val broker = MoviesDetailBroker(
            tmdbSource = ExceptionThrowingSource(throwOnCall = true),
            omdbSource = ExceptionThrowingSource(throwOnCall = false, movie = omdbMovie)
        )

        val result = kotlinx.coroutines.runBlocking {
            broker.getMovieByTitle("Avatar")
        }

        Assert.assertEquals("OMDB: ${omdbMovie.overview}", result?.overview)
    }

    @Test
    fun `when OMDB throws exception should return TMDB data only`() {
        val broker = MoviesDetailBroker(
            tmdbSource = ExceptionThrowingSource(throwOnCall = false, movie = tmdbMovie),
            omdbSource = ExceptionThrowingSource(throwOnCall = true)
        )

        val result = kotlinx.coroutines.runBlocking {
            broker.getMovieByTitle("Avatar")
        }

        Assert.assertEquals("TMDB: ${tmdbMovie.overview}", result?.overview)
    }

    @Test
    fun `when both sources throw exception should return null`() {
        val broker = MoviesDetailBroker(
            tmdbSource = ExceptionThrowingSource(true),
            omdbSource = ExceptionThrowingSource(true)
        )

        val result = kotlinx.coroutines.runBlocking {
            broker.getMovieByTitle("Avatar")
        }

        Assert.assertNull(result)
    }
}