package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

interface GetMovieDetailsUseCase {
    suspend fun execute(title: String): Movie?
}

internal class GetMovieDetailsUseCaseImpl(private val repository: MoviesRepository) : GetMovieDetailsUseCase {
    override suspend fun execute(title: String) = repository.getMovieByTitle(title)
}


