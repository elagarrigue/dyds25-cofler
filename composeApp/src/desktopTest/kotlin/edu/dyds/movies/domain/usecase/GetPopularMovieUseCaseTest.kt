package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlinx.coroutines.test.runTest

class GetPopularMoviesUseCaseTest {

    private val movie1 = Movie(1, "title1", "overview", "releaseDate", "poster", "backdrop", "originalTitle", "originalLanguage", 5.0, 7.0)
    private val movie2 = Movie(2, "title2", "overview", "releaseDate", "poster", "backdrop", "originalTitle", "originalLanguage", 4.0, 5.0)
    private val movie3 = Movie(3, "title3", "overview", "releaseDate", "poster", "backdrop", "originalTitle", "originalLanguage", 6.0, 8.5)

    class FakeRepository(private val movies: List<Movie>) : MoviesRepository {
        override suspend fun getPopularMovies(): List<Movie> = movies
        override suspend fun getMovieByTitle(id: Int): Movie? = null
    }

    @Test
    fun `execute debe ordenar por voteAverage descendente y calificar correctamente`() = runTest {
        // Arrange
        val repository = FakeRepository(listOf(movie1, movie2, movie3))
        val useCase = GetPopularMoviesUseCaseImpl(repository)

        // Act
        val result = useCase.execute()

        // Assert
        assertEquals(3, result.size)
        assertEquals(movie3, result[0].movie) // Mayor voto
        assertEquals(movie1, result[1].movie)
        assertEquals(movie2, result[2].movie)

        assertTrue(result[0].isGoodMovie)
        assertTrue(result[1].isGoodMovie)
        assertFalse(result[2].isGoodMovie) // movie2 con 5.0
    }

    @Test
    fun `execute debe retornar lista vacia si no hay peliculas`() = runTest {
        // Arrange
        val repository = FakeRepository(emptyList())
        val useCase = GetPopularMoviesUseCaseImpl(repository)

        // Act
        val result = useCase.execute()

        // Assert
        assertTrue(result.isEmpty())
    }
}
