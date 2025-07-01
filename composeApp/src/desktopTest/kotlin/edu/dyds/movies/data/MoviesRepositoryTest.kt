package edu.dyds.movies.data

import kotlin.test.Test
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.data.local.LocalMoviesSource
import edu.dyds.movies.data.external.ExternalMoviesSource
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue


class MoviesRepositoryTest {
    private val movie1 = Movie(1, "title", "overview", "releaseDate", "poster", "backdrop", "originalTitle", "originalLanguage", 0.0, 0.0)
    private val movie2 = Movie(2, "title2", "overview2", "releaseDate2", "poster2", "backdrop2", "originalTitle2", "originalLanguage2", 0.0, 0.0)
    private val movie3 = Movie(3, "title3", "overview3", "releaseDate3", "poster3", "backdrop3", "originalTitle3", "originalLanguage3", 0.0, 0.0)

    class LocalMoviesSourceFake : LocalMoviesSource {
        private val cacheMovies: MutableList<Movie> = mutableListOf()

        override fun getMovies() = cacheMovies.toList()

        override fun setMovies(movies: List<Movie>) {
            cacheMovies.clear()
            cacheMovies.addAll(movies)
        }
    }

    class ExternalMoviesSourceFake(private val movie1: Movie, private val movie2: Movie, private val movie3: Movie): ExternalMoviesSource {

        override suspend fun getPopularMovies(): List<Movie> {
            // Simulating an external call
            return listOf(movie1, movie2, movie3)
        }

        override suspend fun getMovieDetails(id: Int): Movie {
            // Simulating an external call
            return when (id) {
                1 -> movie1
                2 -> movie2
                3 -> movie3
                else -> throw Exception("Movie not found")
            }
        }
    }

    class ExternalMoviesSourceErrorFake : ExternalMoviesSource {
        override suspend fun getPopularMovies(): List<Movie> {
            throw Exception("External source error")
        }

        override suspend fun getMovieDetails(id: Int): Movie {
            throw Exception("External source error")
        }
    }

    @Test
     fun `getPopularMovies should return cached movies when available`() = runTest{
        // ARRANGE
        val localMoviesSource = LocalMoviesSourceFake()
        localMoviesSource.setMovies(listOf(movie1, movie2, movie3))
        val externalMoviesSource = ExternalMoviesSourceFake(movie1, movie2, movie3)
        val moviesRepository = MoviesRepositoryImpl(localMoviesSource, externalMoviesSource)

        // ACT
        val result = moviesRepository.getPopularMovies()

        // ASSERT
        assertEquals(result, listOf(movie1, movie2, movie3))
    }

    @Test
    fun `getPopularMovies should fetch from external source when cache is empty`() = runTest {
        // ARRANGE
        val localMoviesSource = LocalMoviesSourceFake()
        val externalMoviesSource = ExternalMoviesSourceFake(movie1, movie2, movie3)
        val moviesRepository = MoviesRepositoryImpl(localMoviesSource, externalMoviesSource)

        // ACT
        val result = moviesRepository.getPopularMovies()

        // ASSERT
        assertEquals(result, listOf(movie1, movie2, movie3))
    }

    @Test
    fun `getMovieDetails should return movie details from external source`() = runTest {
        // ARRANGE
        val localMoviesSource = LocalMoviesSourceFake()
        val externalMoviesSource = ExternalMoviesSourceFake(movie1, movie2, movie3)
        val moviesRepository = MoviesRepositoryImpl(localMoviesSource, externalMoviesSource)

        // ACT
        val result = moviesRepository.getMovieDetails(1)

        // ASSERT
        assertEquals(result, movie1)
    }

    @Test
    fun `getMovieDetails should return null when movie not found`() = runTest {
        // ARRANGE
        val localMoviesSource = LocalMoviesSourceFake()
        val externalMoviesSource = ExternalMoviesSourceFake(movie1, movie2, movie3)
        val moviesRepository = MoviesRepositoryImpl(localMoviesSource, externalMoviesSource)

        // ACT
        val result = moviesRepository.getMovieDetails(999) // Non-existent movie ID

        // ASSERT
        assertNull(result)
    }

    @Test
    fun `getPopularMovies should return empty list when external source fails`() = runTest {
        // arrange
        val localMoviesSource = LocalMoviesSourceFake()
        val externalMoviesSource = ExternalMoviesSourceErrorFake()
        val moviesRepository = MoviesRepositoryImpl(localMoviesSource, externalMoviesSource)

        // act
        val result = moviesRepository.getPopularMovies()

        // assert
        assertTrue(result.isEmpty())
    }

}