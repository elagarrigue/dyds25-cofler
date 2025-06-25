import edu.dyds.movies.domain.entity.Movie
import kotlin.test.Test
import edu.dyds.movies.data.local.LocalMoviesImpl


class LocalDataSourceTest {
    private val movie1 = Movie(1, "title", "overview", "releaseDate", "poster", "backdrop", "originalTitle", "originalLanguage", 0.0, 0.0)
    private val movie2 = Movie(2, "title2", "overview2", "releaseDate2", "poster2", "backdrop2", "originalTitle2", "originalLanguage2", 0.0, 0.0)
    private val movie3 = Movie(3, "title3", "overview3", "releaseDate3", "poster3", "backdrop3", "originalTitle3", "originalLanguage3", 0.0, 0.0)
    private val movie4 = Movie(4, "title4", "overview4", "releaseDate4", "poster4", "backdrop4", "originalTitle4", "originalLanguage4", 0.0, 0.0)
    private val movie5 = Movie(5, "title5", "overview5", "releaseDate5", "poster5", "backdrop5", "originalTitle5", "originalLanguage5", 0.0, 0.0)

    @Test
    fun `get movies should return cached movies`() {
        // ARRANGE
        val localDataSource = LocalMoviesImpl()
        val initialMovies = listOf(movie1, movie2, movie3)
        localDataSource.setMovies(initialMovies)

        // ACT
        val result = localDataSource.getMovies()

        // ASSERT
        assert(result == initialMovies)
    }

    @Test
    fun `set movies should update cached movies`() {
        // ARRANGE
        val localDataSource = LocalMoviesImpl()
        val newMovies = listOf(movie4, movie5)

        // ACT
        localDataSource.setMovies(newMovies)
        val result = localDataSource.getMovies()

        // ASSERT
        assert(result == newMovies)
    }

    @Test
    fun `get movies should return empty list when no movies are set`() {
        // ARRANGE
        val localDataSource = LocalMoviesImpl()

        // ACT
        val result = localDataSource.getMovies()

        // ASSERT
        assert(result.isEmpty())
    }
}