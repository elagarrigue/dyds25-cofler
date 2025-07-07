package edu.dyds.movies.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.dyds.movies.presentation.detail.DetailViewModel
import edu.dyds.movies.presentation.home.HomeViewModel
import edu.dyds.movies.data.MoviesRepositoryImpl
import edu.dyds.movies.data.external.ExternalMoviesSourceImpl
import edu.dyds.movies.data.external.broker.MoviesDetailBroker
import edu.dyds.movies.data.external.omdb.OMDBExternalMoviesSource
import edu.dyds.movies.data.external.tmdb.TMDBExternalMoviesSource
import edu.dyds.movies.data.local.LocalMoviesImpl
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCaseImpl
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCaseImpl
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

private const val TMBD_KEY = "d18da1b5da16397619c688b0263cd281"
private const val OMDB_API_KEY = "a96e7f78"

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
                    parameters.append("api_key", TMBD_KEY)
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 5000
            }
        }

    private val omdbHttpClient =
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "www.omdbapi.com"
                    parameters.append("apikey", OMDB_API_KEY)
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 5000
            }
        }

    private val localMoviesImpl = LocalMoviesImpl()

    private val externalMoviesTMDB = TMDBExternalMoviesSource(tmdbHttpClient)
    private val externalMoviesOMDB = OMDBExternalMoviesSource(omdbHttpClient)

    private val moviesDetailBroker = MoviesDetailBroker(
        tmdbSource = externalMoviesTMDB,
        omdbSource = externalMoviesOMDB
    )

    private val externalMoviesSource = ExternalMoviesSourceImpl(
        moviesListSource = externalMoviesTMDB,
        moviesDetailSource = moviesDetailBroker
    )

    private val moviesRepositoryImpl = MoviesRepositoryImpl(localMoviesImpl, externalMoviesSource)

    private val getPopularMoviesUseCase = GetPopularMoviesUseCaseImpl(moviesRepositoryImpl)
    private val getMovieDetailsUseCase = GetMovieDetailsUseCaseImpl(moviesRepositoryImpl)

    @Composable
    fun getPopularMoviesViewModel(): HomeViewModel {
        return viewModel { HomeViewModel(getPopularMoviesUseCase) }
    }

    @Composable
    fun getMovieDetailsViewModel(): DetailViewModel {
        return viewModel { DetailViewModel(getMovieDetailsUseCase) }
    }
}