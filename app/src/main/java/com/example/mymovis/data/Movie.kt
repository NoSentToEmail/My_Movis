package com.example.mymovis.data

class Movie(
     var id: Int,
     var voteCount: Int,
     var title: String,
     var originalTitle: String,
     var overView: String,
     var posterPath: String,
     var bigPosterPath: String,
     var backDropPath: String,
     var voteAverage: Double,
     var releaseDate: String
) {
     override fun toString(): String {
          return "Movie(id=$id, voteCount=$voteCount, title='$title', originalTitle='$originalTitle', " +
                  "overView='$overView', posterPath='$posterPath', bigPosterPath='$bigPosterPath', " +
                  "backDropPath='$backDropPath', voteAverage=$voteAverage, releaseDate='$releaseDate')"
     }
}