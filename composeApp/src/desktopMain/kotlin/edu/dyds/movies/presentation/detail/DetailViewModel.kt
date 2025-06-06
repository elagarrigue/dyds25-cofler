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

    fun getMovieDetail(id: Int) {
        viewModelScope.launch {
            movieDetailStateMutableStateFlow.emit(
                UiState(isLoading = true)
            )
            movieDetailStateMutableStateFlow.emit(
                UiState(
                    isLoading = false,
                    movie = getMovieDetailsUseCase.execute(id)
                )
            )
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val movie: Movie? = null,
    )
}