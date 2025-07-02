package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest

class GetMovieDetailUseCaseTest {

    private val movie1 = Movie(
        1, "title", "overview", "releaseDate", "poster", "backdrop",
        "originalTitle", "originalLanguage", 5.0, 6.0
    )

    class FakeRepository(private val movie: Movie) : MoviesRepository {
        override suspend fun getPopularMovies(): List<Movie> = emptyList()
        override suspend fun getMovieByTitle(id: Int): Movie? =
            if (id == movie.id) movie else null
    }

    @Test
    fun `execute debe retornar una pelicula valida si existe`() = runTest {
        // Arrange
        val repository = FakeRepository(movie1)
        val useCase = GetMovieDetailsUseCaseImpl(repository)

        // Act
        val result = useCase.execute(1)

        // Assert
        assertEquals(movie1, result)
    }

    @Test
    fun `execute debe retornar nulo si la pelicula no es valida`() = runTest {
        // Arrange
        val repository = FakeRepository(movie1)
        val useCase = GetMovieDetailsUseCaseImpl(repository)

        // Act
        val result = useCase.execute(2)

        // Assert
        assertNull(result)
    }
}
