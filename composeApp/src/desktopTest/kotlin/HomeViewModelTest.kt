import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import edu.dyds.movies.presentation.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = CoroutineScope(testDispatcher)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val getPopularMoviesUseCase = object : GetPopularMoviesUseCase {
        override suspend fun execute(): List<QualifiedMovie> {
            return listOf(QualifiedMovie(Movie(
                1,
                "Movie 1",
                "the movie 1 overview",
                "21/10/2023",
                "poster url",
                "backdrop url",
                "Original Movie 1",
                "en",
                10.0,
                8.0)
            , isGoodMovie = true
            ))
        }
    }

    @Test
    fun `get movie should emit loading and data states`() = runTest {
        // Arrange
        val homeViewModel = HomeViewModel(getPopularMoviesUseCase)

        val events: ArrayList<HomeViewModel.UiState> = arrayListOf()
        testScope.launch {
            homeViewModel.moviesStateFlow.collect { state ->
                events.add(state)
            }
        }

        // Act
        homeViewModel.getAllMovies()

        // Assert
        assertEquals(
            expected = HomeViewModel.UiState(isLoading = true, movies = emptyList()),
            actual = events[0]
        )
        assertEquals(
            expected = HomeViewModel.UiState(
                isLoading = false,
                listOf(QualifiedMovie(Movie(
                    1,
                    "Movie 1",
                    "the movie 1 overview",
                    "21/10/2023",
                    "poster url",
                    "backdrop url",
                    "Original Movie 1",
                    "en",
                    10.0,
                    8.0)
                    , isGoodMovie = true
                ))
            ),
            actual = events[1]
        )
    }
}