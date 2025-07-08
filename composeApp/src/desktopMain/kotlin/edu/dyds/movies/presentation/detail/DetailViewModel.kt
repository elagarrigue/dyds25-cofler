package edu.dyds.movies.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val getMovieDetailsUseCase: GetMovieDetailsUseCase) : ViewModel() {
    private val movieDetailStateMutableStateFlow = MutableStateFlow(UiState())
    val movieDetailStateFlow: Flow<UiState> = movieDetailStateMutableStateFlow

    fun getMovieByTitle(title: String) {
        viewModelScope.launch {
            movieDetailStateMutableStateFlow.emit(
                UiState(
                    isLoading = false,
                    movie = getMovieDetailsUseCase.execute(title)
                )
            )
        }
    }

    data class UiState(
        val isLoading: Boolean = true,
        val movie: Movie? = null,
    )
}