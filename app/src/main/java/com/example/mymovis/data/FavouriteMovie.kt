package com.example.mymovis.data

import android.annotation.SuppressLint
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_movies")
class FavouriteMovie(
    id: Int,
    voteCount: Int,
    title: String,
    originalTitle: String,
    overView: String,
    posterPath: String,
    bigPosterPath: String,
    backDropPath: String,
    voteAverage: Double,
    releaseDate: String
) : Movie(
    id,
    voteCount,
    title,
    originalTitle,
    overView,
    posterPath,
    bigPosterPath,
    backDropPath,
    voteAverage,
    releaseDate
) {

    @Ignore
    constructor(movie: Movie) : this(
        movie.id,
        movie.voteCount,
        movie.title,
        movie.originalTitle,
        movie.overView,
        movie.posterPath,
        movie.bigPosterPath,
        movie.backDropPath,
        movie.voteAverage,
        movie.releaseDate
    )
}

