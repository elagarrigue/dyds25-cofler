package edu.dyds.movies.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val getPopularMoviesUseCase: GetPopularMoviesUseCase) : ViewModel() {
    private val moviesStateMutableStateFlow = MutableStateFlow(UiState())
    val moviesStateFlow: Flow<UiState> = moviesStateMutableStateFlow

    fun getAllMovies() {
        viewModelScope.launch {
            moviesStateMutableStateFlow.emit(
                UiState(isLoading = true)
            )
            moviesStateMutableStateFlow.emit(
                UiState(
                    isLoading = false,
                    movies = getPopularMoviesUseCase.execute()
                )
            )
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val movies: List<QualifiedMovie> = emptyList(),
    )
}