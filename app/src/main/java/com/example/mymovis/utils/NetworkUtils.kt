package com.example.mymovis.utils

import android.net.Uri
import android.os.AsyncTask
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NetworkUtils {
    private val API_KEY = "e12b9ed2023f9ce765d45a1deddfe191"
    private val BASE_URL = "https://api.themoviedb.org/3/discover/movie"
    private val PARAMS_API_KEY = "api_key"
    private val PARAMS_LANGUAGE = "language"
    private val PARAMS_SORT_BY = "sort_by"
    private val PARAMS_PAGE = "page"

    private val LANGUAGE_VALUE = "ru-RU"
    private val SORT_BY_POPULARITY = "popularity.desc"
    private val SORT_BY_TOP_RATED = "vote_average.desc"


    val POPULARITY = 0
    val TOP_RATED = 1


    private fun buildURL(sortBy: Int, page: Int): URL? {
        var result: URL? = null
        val methodSort = if (sortBy == POPULARITY) {
            SORT_BY_POPULARITY
        } else {
            SORT_BY_TOP_RATED
        }
        val url = Uri.parse(BASE_URL).buildUpon()
            .appendQueryParameter(PARAMS_API_KEY, API_KEY)
            .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
            .appendQueryParameter(PARAMS_SORT_BY, methodSort)
            .appendQueryParameter(PARAMS_PAGE, page.toString())
            .build()
        try {
            result = URL(url.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }


    fun getJSONFromNetwork(sortBy: Int, page: Int): JSONObject? {
        var result: JSONObject? = null
        val url = NetworkUtils().buildURL(sortBy, page)
        try {
            result = JSONLoadTask().execute(url).get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }


    private class JSONLoadTask : AsyncTask<URL, Void, JSONObject>() {
        override fun doInBackground(vararg params: URL?): JSONObject? {
            var result: JSONObject? = null

            if (params == null || params.size == 0) {
                return result
            }
            var connection:HttpURLConnection? = null
            try {
                connection = params[0]?.openConnection() as HttpURLConnection
                val inputStream: InputStream = connection.inputStream
                val inputStreamReader: InputStreamReader = InputStreamReader(inputStream)
                val reader: BufferedReader = BufferedReader(inputStreamReader)
                val builder = StringBuilder()
                var line = reader.readLine()
                while (line != null) {
                    builder.append(line)
                    line = reader.readLine()

                }
                result = JSONObject(builder.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (connection != null) {
                    connection.disconnect()
                }
            }
            return result
        }
    }
}
