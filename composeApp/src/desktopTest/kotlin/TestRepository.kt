import kotlin.test.Test
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository
import edu.dyds.movies.data.local.LocalMoviesSource
import edu.dyds.movies.data.external.ExternalMoviesSource
import kotlinx.coroutines.test.runTest


class TestRepository {
    val Movie1 = Movie(1, "title", "overview", "releaseDate", "poster", "backdrop", "originalTitle", "originalLanguage", 0.0, 0.0)
    val Movie2 = Movie(2, "title2", "overview2", "releaseDate2", "poster2", "backdrop2", "originalTitle2", "originalLanguage2", 0.0, 0.0)
    val Movie3 = Movie(3, "title3", "overview3", "releaseDate3", "poster3", "backdrop3", "originalTitle3", "originalLanguage3", 0.0, 0.0)

    class ImplLocalMoviesSource : LocalMoviesSource {
        private val cacheMovies: MutableList<Movie> = mutableListOf()

        override fun getMovies() = cacheMovies.toList()

        override fun setMovies(movies: List<Movie>) {
            cacheMovies.clear()
            cacheMovies.addAll(movies)
        }
    }

    class ImplExternalMoviesSource: ExternalMoviesSource {
        val Movie1 = Movie(
            1,
            "title",
            "overview",
            "releaseDate",
            "poster",
            "backdrop",
            "originalTitle",
            "originalLanguage",
            0.0,
            0.0
        )
        val Movie2 = Movie(
            2,
            "title2",
            "overview2",
            "releaseDate2",
            "poster2",
            "backdrop2",
            "originalTitle2",
            "originalLanguage2",
            0.0,
            0.0
        )
        val Movie3 = Movie(
            3,
            "title3",
            "overview3",
            "releaseDate3",
            "poster3",
            "backdrop3",
            "originalTitle3",
            "originalLanguage3",
            0.0,
            0.0
        )

        override suspend fun getPopularMovies(): List<Movie> {
            // Simulating an external call
            return listOf(Movie1, Movie2, Movie3)
        }

        override suspend fun getMovieDetails(id: Int): Movie {
            // Simulating an external call
            return when (id) {
                1 -> Movie1
                2 -> Movie2
                3 -> Movie3
                else -> throw Exception("Movie not found")
            }
        }
    }

    class MoviesRepositoryImpl(
        private val localMoviesSource: ImplLocalMoviesSource,
        private val externalMoviesSource: ExternalMoviesSource
    ) : MoviesRepository {

        override suspend fun getPopularMovies(): List<Movie> {
            val localMovies = localMoviesSource.getMovies()
            return localMovies.ifEmpty {
                try {
                    externalMoviesSource.getPopularMovies().apply {
                        localMoviesSource.setMovies(this)
                    }
                } catch (e: Exception) {
                    emptyList()
                }
            }
        }

        override suspend fun getMovieDetails(id: Int): Movie? =
            try {
                externalMoviesSource.getMovieDetails(id)
            } catch (e: Exception) {
                null
            }
    }

    @Test
     fun `getPopularMovies should return cached movies when available`() = runTest{
        // arrange
        val localMoviesSource = ImplLocalMoviesSource()
        localMoviesSource.setMovies(listOf(Movie1, Movie2))
        val externalMoviesSource = ImplExternalMoviesSource()
        val moviesRepository = MoviesRepositoryImpl(localMoviesSource, externalMoviesSource)

        // act
        val result = moviesRepository.getPopularMovies()

        // assert
        assert(result == listOf(Movie1, Movie2))
    }

    @Test
    fun `getPopularMovies should fetch from external source when cache is empty`() = runTest {
        // arrange
        val localMoviesSource = ImplLocalMoviesSource()
        val externalMoviesSource = ImplExternalMoviesSource()
        val moviesRepository = MoviesRepositoryImpl(localMoviesSource, externalMoviesSource)

        // act
        val result = moviesRepository.getPopularMovies()

        // assert
        assert(result == listOf(Movie1, Movie2, Movie3))
    }

    @Test
    fun `getMovieDetails should return movie details from external source`() = runTest {
        // arrange
        val localMoviesSource = ImplLocalMoviesSource()
        val externalMoviesSource = ImplExternalMoviesSource()
        val moviesRepository = MoviesRepositoryImpl(localMoviesSource, externalMoviesSource)

        // act
        val result = moviesRepository.getMovieDetails(1)

        // assert
        assert(result == Movie1)
    }

    @Test
    fun `getMovieDetails should return null when movie not found`() = runTest {
        // arrange
        val localMoviesSource = ImplLocalMoviesSource()
        val externalMoviesSource = ImplExternalMoviesSource()
        val moviesRepository = MoviesRepositoryImpl(localMoviesSource, externalMoviesSource)

        // act
        val result = moviesRepository.getMovieDetails(999) // Non-existent movie ID

        // assert
        assert(result == null)
    }
    //------------------------- Preguntar si podemos usar mocks ------------------------------//
    @Test
    fun `getPopularMovies should return empty list when external source fails`() = runTest {
        // arrange
        val localMoviesSource = ImplLocalMoviesSource()
        val externalMoviesSource = object : ExternalMoviesSource {
            override suspend fun getPopularMovies(): List<Movie> {
                throw Exception("External source error")
            }

            override suspend fun getMovieDetails(id: Int): Movie {
                throw Exception("External source error")
            }
        }
        val moviesRepository = MoviesRepositoryImpl(localMoviesSource, externalMoviesSource)

        // act
        val result = moviesRepository.getPopularMovies()

        // assert
        assert(result.isEmpty())
    }
    //--------------------------------------------------------------------------------------//
}