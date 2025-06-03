package edu.dyds.movies.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.dyds.movies.presentation.detail.MovieDetailsViewModel
import edu.dyds.movies.presentation.home.PopularMoviesViewModel
import edu.dyds.movies.data.MoviesRepositoryImpl
import edu.dyds.movies.data.external.ExternalMoviesImpl
import edu.dyds.movies.data.local.LocalMoviesImpl
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCaseImpl
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCaseImpl
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

private const val API_KEY = "d18da1b5da16397619c688b0263cd281"

object MoviesDependencyInjector {
    private val tmdbHttpClient =
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.themoviedb.org"
                    parameters.append("api_key", API_KEY)
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 5000
            }
        }

    private val localMoviesImpl = LocalMoviesImpl()
    private val externalMoviesImpl = ExternalMoviesImpl(tmdbHttpClient)
    private val moviesRepositoryImpl = MoviesRepositoryImpl(localMoviesImpl, externalMoviesImpl)
    private val getPopularMoviesUseCase = GetPopularMoviesUseCaseImpl(moviesRepositoryImpl)
    private val getMovieDetailsUseCase = GetMovieDetailsUseCaseImpl(moviesRepositoryImpl)

    @Composable
    fun getPopularMoviesViewModel(): PopularMoviesViewModel {
        return viewModel { PopularMoviesViewModel(getPopularMoviesUseCase) }
    }

    @Composable
    fun getMovieDetailsViewModel(): MovieDetailsViewModel {
        return viewModel { MovieDetailsViewModel(getMovieDetailsUseCase) }
    }
}