import edu.dyds.movies.domain.entity.Movie
import kotlin.test.Test



class TestLocalDataSource {

    interface LocalDataSource {
        fun getMovies(): List<Movie>
        fun setMovies(movies: List<Movie>)
    }

    val Movie1 = Movie(1, "title", "overview", "releaseDate", "poster", "backdrop", "originalTitle", "originalLanguage", 0.0, 0.0)
    val Movie2 = Movie(2, "title2", "overview2", "releaseDate2", "poster2", "backdrop2", "originalTitle2", "originalLanguage2", 0.0, 0.0)
    val Movie3 = Movie(3, "title3", "overview3", "releaseDate3", "poster3", "backdrop3", "originalTitle3", "originalLanguage3", 0.0, 0.0)
    val Movie4 = Movie(4, "title4", "overview4", "releaseDate4", "poster4", "backdrop4", "originalTitle4", "originalLanguage4", 0.0, 0.0)
    val Movie5 = Movie(5, "title5", "overview5", "releaseDate5", "poster5", "backdrop5", "originalTitle5", "originalLanguage5", 0.0, 0.0)


    class LocalDataSourceImpl : LocalDataSource {
        private val cacheMovies: MutableList<Movie> = mutableListOf()

        override fun getMovies() = cacheMovies.toList()

        override fun setMovies(movies: List<Movie>) {
            cacheMovies.clear()
            cacheMovies.addAll(movies)
        }
    }

    @Test
    fun `get movies should return cached movies`() {
        // arrange
        val localDataSource = LocalDataSourceImpl()
        val initialMovies = listOf(Movie1, Movie2, Movie3)
        localDataSource.setMovies(initialMovies)

        // act
        val result = localDataSource.getMovies()

        // assert
        assert(result == initialMovies)
    }

    @Test
    fun `set movies should update cached movies`() {
        // arrange
        val localDataSource = LocalDataSourceImpl()
        val newMovies = listOf(Movie4, Movie5)

        // act
        localDataSource.setMovies(newMovies)
        val result = localDataSource.getMovies()

        // assert
        assert(result == newMovies)
    }

    @Test
    fun `get movies should return empty list when no movies are set`() {
        // arrange
        val localDataSource = LocalDataSourceImpl()

        // act
        val result = localDataSource.getMovies()

        // assert
        assert(result.isEmpty())
    }
}