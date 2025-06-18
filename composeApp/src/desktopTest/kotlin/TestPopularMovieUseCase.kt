import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCaseImpl
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository
import kotlin.test.Test
import kotlinx.coroutines.test.runTest


class TestGetPopularMoviesUseCase {
    private val movie1 = Movie(1, "title1", "overview", "releaseDate", "poster", "backdrop", "originalTitle", "originalLanguage", 5.0, 7.0)
    private val movie2 = Movie(2, "title2", "overview", "releaseDate", "poster", "backdrop", "originalTitle", "originalLanguage", 4.0, 5.0)
    private val movie3 = Movie(3, "title3", "overview", "releaseDate", "poster", "backdrop", "originalTitle", "originalLanguage", 6.0, 8.5)

    class FakeRepository(private val movies: List<Movie>) : MoviesRepository {
        override suspend fun getPopularMovies(): List<Movie> = movies
        override suspend fun getMovieDetails(id: Int): Movie? = null
    }

    @Test
    fun `execute debe ordenar por voteAverage descendente y calificar correctamente`() = runTest {
        //arrange
        val repository = FakeRepository(listOf(movie1, movie2, movie3))
        val useCase = GetPopularMoviesUseCaseImpl(repository)

        //act
        val result = useCase.execute()

        println("Result: $result")

        //assert
        assert(result.size == 3)
        assert(result[0].movie == movie3) // Mayor voto
        assert(result[0].isGoodMovie)
        assert(result[1].isGoodMovie)
        assert(!result[2].isGoodMovie) // movie2 con 5.0
    }

    @Test
    fun `execute debe retornar lista vacia si no hay peliculas`() = runTest {
       //arrange
        val repository = FakeRepository(emptyList())
        val useCase = GetPopularMoviesUseCaseImpl(repository)

        //act
        val result = useCase.execute()

        //assert
        assert(result.isEmpty())
    }
}
