import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCaseImpl
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class TestGetMovieDetailUseCase {
    private val movie1 = Movie(1, "title", "overview", "releaseDate", "poster", "backdrop", "originalTitle", "originalLanguage", 5.0, 6.0)

    class FakeRepository(private val movie: Movie) : MoviesRepository {
        override suspend fun getPopularMovies(): List<Movie> = emptyList()

        override suspend fun getMovieDetails(id: Int): Movie? =
            if (id == movie.id) movie else null
    }

    @Test
    fun `execute debe retornar una película válida si existe`() = runTest {
        // arrange
        val repository = FakeRepository(movie1)
        val useCase = GetMovieDetailsUseCaseImpl(repository)

        // act
        val result = useCase.execute(1)

        // assert
        assert(result == movie1)
    }

    @Test
    fun `execute debe retornar nulo si la pelicula no es valida`() = runTest {
        // arrange
        val repository = FakeRepository(movie1)
        val useCase = GetMovieDetailsUseCaseImpl(repository)

        // act
        val result = useCase.execute(2)

        // assert
        assert(result == null)
    }
}
