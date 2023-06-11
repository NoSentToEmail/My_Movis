package com.example.mymovis.utils

import android.util.Log
import com.example.mymovis.data.Movie
import com.example.mymovis.data.Review
import com.example.mymovis.data.Trailer
import org.json.JSONObject

class JSONUtils {
    //вся информация о фильме
    private val KEY_RESULT = "results"

    //для отзывов
    private val KEY_AUTHOR = "author"
    private val KEY_CONTENT = "content"

    // для видео
    private val KEY_OF_VIDEO = "key"
    private val KEY_NAME = "name"
    private val BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v="

    //вся информация о фильме
    private val KEY_VOTE_COUNT = "vote_count"
    private val KEY_ID = "id"
    private val KEY_TITLE = "title"
    private val KEY_ORIGINAL_TITLE = "original_title"
    private val KEY_OVERVIEW = "overview"
    private val KEY_POSTER_PATH = "poster_path"
    private val KEY_BACKDROP_PATH = "backdrop_path"
    private val KEY_VOTE_AVARAGE = "vote_average"
    private val KEY_RELEASE_DATA = "release_date"


    val BASE_POSTER_URL = "https://image.tmdb.org/t/p/"
    val SMALL_POSTER_SIZE = "w185"
    val BIG_POSTER_SIZE = "w780"

    fun getReviewsFromJSON(jsonObject: JSONObject): ArrayList<Review> {
        val result = ArrayList<Review>()
        if (jsonObject == null) {
            Log.d("JSONUtils", "JSON object is null")
            return result
        }
        try {
            val jsonArray = jsonObject.getJSONArray(KEY_RESULT)
            for (i in 0 until jsonArray.length()) {
                val objectReview: JSONObject = jsonArray.getJSONObject(i)
                val author = objectReview.getString(KEY_AUTHOR)
                val content = objectReview.getString(KEY_CONTENT)
                val review = Review(author, content)
                result.add(review)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun getTrailerFromJSON(jsonObject: JSONObject): ArrayList<Trailer> {
        val result = ArrayList<Trailer>()
        if (jsonObject == null) {
            Log.d("JSONUtils", "JSON object is null")
            return result
        }
        try {
            val jsonArray = jsonObject.getJSONArray(KEY_RESULT)
            for (i in 0 until jsonArray.length()) {
                val objectTrailer: JSONObject = jsonArray.getJSONObject(i)
                val key = BASE_YOUTUBE_URL + objectTrailer.getString(KEY_OF_VIDEO)
                val name = objectTrailer.getString(KEY_NAME)
                val trailer = Trailer(key, name)
                result.add(trailer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }


    fun getMoviesFromJSON(jsonObject: JSONObject): ArrayList<Movie> {
        val result = ArrayList<Movie>()
        if (jsonObject == null) {
            Log.d("JSONUtils", "JSON object is null")
            return result
        }
        try {
            val jsonArray = jsonObject.getJSONArray(KEY_RESULT)
            for (i in 0 until jsonArray.length()) {
                val objectMovie: JSONObject = jsonArray.getJSONObject(i)
                val id = objectMovie.getInt(KEY_ID)
                val voteCount = objectMovie.getInt(KEY_VOTE_COUNT)
                val title = objectMovie.getString(KEY_TITLE)
                val originalTitle = objectMovie.getString(KEY_ORIGINAL_TITLE)
                val overView = objectMovie.getString(KEY_OVERVIEW)
                val posterPath =
                    BASE_POSTER_URL + SMALL_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH)
                val bigPosterPath =
                    BASE_POSTER_URL + BIG_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH)
                val backdrop_Path = objectMovie.getString(KEY_BACKDROP_PATH)
                val voteAvarage = objectMovie.getDouble(KEY_VOTE_AVARAGE)
                val releaseData = objectMovie.getString(KEY_RELEASE_DATA)

                val movie: Movie = Movie(
                    id,
                    voteCount,
                    title,
                    originalTitle,
                    overView,
                    posterPath,
                    bigPosterPath,
                    backdrop_Path,
                    voteAvarage,
                    releaseData
                )
                result.add(movie)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

}