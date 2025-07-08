package edu.dyds.movies.presentation.detail

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCase
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
class DetailViewModelTest {
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

    private val getMovieDetailsUseCase = object : GetMovieDetailsUseCase {
        override suspend fun execute(title: String): Movie {
            return Movie(
                1,
                "Movie $title",
                "the movie $title overview",
                "21/10/2023",
                "poster url",
                "backdrop url",
                "Original Movie $title",
                "en",
                10.0,
                8.0
            )
        }
    }

    @Test
    fun `get movie should emit loading and data states`() = runTest {
        // Arrange
        val detailViewModel = DetailViewModel(getMovieDetailsUseCase)

        val events: ArrayList<DetailViewModel.UiState> = arrayListOf()
        testScope.launch {
            detailViewModel.movieDetailStateFlow.collect { state ->
                events.add(state)
            }
        }

        // Act
        detailViewModel.getMovieByTitle("1")

        // Assert
        assertEquals(
            expected = DetailViewModel.UiState(isLoading = true, movie = null),
            actual = events[0]
        )
        assertEquals(
            expected = DetailViewModel.UiState(
                isLoading = false,
                movie = Movie(
                    1,
                    "Movie 1",
                    "the movie 1 overview",
                    "21/10/2023",
                    "poster url",
                    "backdrop url",
                    "Original Movie 1",
                    "en",
                    10.0,
                    8.0
                )
            ),
            actual = events[1]
        )
    }
}