package com.example.mymovis.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database: MovieDatabase
    private val movie: LiveData<List<Movie>>
    private val favourite_movies: LiveData<List<FavouriteMovie>>

    init {
        database = MovieDatabase.getIstance(application)
        movie = database.movieDao().getAllMovies()
        favourite_movies = database.movieDao().getAllFavouriteMovies()
    }


    fun getMovies(): LiveData<List<Movie>> {
        return movie
    }

    fun getFavouriteMovies(): LiveData<List<FavouriteMovie>> {
        return favourite_movies
    }

    suspend fun getMovieById(id: Int): Movie {
        return withContext(Dispatchers.IO) {
            database.movieDao().getMovieById(id)
        }
    }

    suspend fun getFavouriteMovieById(id: Int): FavouriteMovie {
        return withContext(Dispatchers.IO) {
            database.movieDao().getFavouriteMovieById(id)
        }
    }

    fun update(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        database.movieDao().updateMovie(movie)
    }

    fun insert(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        database.movieDao().insertMovie(movie)
    }

    fun delete(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        database.movieDao().deleteMovies(movie)
    }

    fun deleteAllMovies() = viewModelScope.launch(Dispatchers.IO) {
        database.movieDao().deleteAllMovies()
    }

    fun insertFavouriteMovies(movie: FavouriteMovie) = viewModelScope.launch(Dispatchers.IO) {
        database.movieDao().insertFavouriteMovie(movie)
    }

    fun deleteFavouriteMovies(movie: FavouriteMovie) = viewModelScope.launch(Dispatchers.IO) {
        database.movieDao().deleteFavouriteMovies(movie)
    }

}