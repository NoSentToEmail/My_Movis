package com.example.mymovis.utils

import com.example.mymovis.data.Movie
import org.json.JSONObject

class JSONUtils {

    private val KEY_RESULT = "results"
    private val KEY_VOTE_COUNT = "vote_count"
    private val KEY_ID = "id"
    private val KEY_TITLE = "title"
    private val KEY_ORIGINAL_TITLE = "original_title"
    private val KEY_OVERVIEW = "overview"
    private val KEY_POSTER_PATH = "poster_path"
    private val KEY_BACKDROP_PATH = "backdrop_path"
    private val KEY_VOTE_AVARAGE = "vote_average"
    private val KEY_RELEASE_DATA = "release_date"

    fun getMoviesFromJSON(jsonObject: JSONObject): ArrayList<Movie> {
        val result = ArrayList<Movie>()
        if (jsonObject == null) {
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
                val posterPath = objectMovie.getString(KEY_POSTER_PATH)
                val backdrop_Path = objectMovie.getString(KEY_BACKDROP_PATH)
                val voteAvarage = objectMovie.getDouble(KEY_VOTE_AVARAGE)
                val releaseData = objectMovie.getString(KEY_RELEASE_DATA)

                val movie: Movie = Movie(id,
                    voteCount,
                    title,
                    originalTitle,
                    overView,
                    posterPath,
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