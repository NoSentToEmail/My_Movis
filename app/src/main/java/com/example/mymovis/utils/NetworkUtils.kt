package com.example.mymovis.utils

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import androidx.loader.content.AsyncTaskLoader
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NetworkUtils {


    private val API_KEY = "e12b9ed2023f9ce765d45a1deddfe191"
    private val BASE_URL = "https://api.themoviedb.org/3/discover/movie"

    private val BASE_URL_VIDEOS = "https://api.themoviedb.org/3/movie/%s/videos"
    private val BASE_URL_REVIEWS = "https://api.themoviedb.org/3/movie/%s/reviews"


    private val PARAMS_API_KEY = "api_key"
    private val PARAMS_LANGUAGE = "language"
    private val PARAMS_SORT_BY = "sort_by"
    private val PARAMS_PAGE = "page"
    private val PARAM_MIN_VOTE_COUNT = "vote_count.gte"

    private val LANGUAGE_VALUE = "ru-RU"
    private val SORT_BY_POPULARITY = "popularity.desc"
    private val SORT_BY_TOP_RATED = "vote_average.desc"
    private val MIN_VOTE_COUNT_VALUE = "1000"


    val POPULARITY = 0
    val TOP_RATED = 1


     fun buildURL(sortBy: Int, page: Int): URL? {
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
            .appendQueryParameter(PARAM_MIN_VOTE_COUNT, MIN_VOTE_COUNT_VALUE)
            .appendQueryParameter(PARAMS_PAGE, page.toString())
            .build()
        try {
            result = URL(url.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    //Добавление отзывов с инета
     fun buildURLToREVIEWS(id: Int): URL? {
        val url = Uri.parse(String.format(BASE_URL_REVIEWS, id)).buildUpon()
            .appendQueryParameter(PARAMS_API_KEY, API_KEY).build()
        try {
            return URL(url.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    //Добавление отзывов с инета(реализация)
    fun getJSONForReviews(id: Int): JSONObject? {
        var result: JSONObject? = null
        val url = buildURLToREVIEWS(id)
        try {
            result = JSONLoadTask().execute(url).get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    //Добавление получения видео с инета

     fun buildURLToVideos(id: Int): URL? {
        val url = Uri.parse(String.format(BASE_URL_VIDEOS, id)).buildUpon()
            .appendQueryParameter(PARAMS_API_KEY, API_KEY)
            .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE).build()
        try {
            return URL(url.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    //Добавление получения видео с инета(реализация)
    fun getJSONForVideos(id: Int): JSONObject? {
        var result: JSONObject? = null
        val url = buildURLToVideos(id)
        try {
            result = JSONLoadTask().execute(url).get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    //Подгрузка данных с сети без ограничений
    class JSONLoader(context: Context, private val bundle: Bundle?) : AsyncTaskLoader<JSONObject>(context) {

        private lateinit var onStartLoadingListener: OnStartLoadingListener
        interface OnStartLoadingListener {
            fun onStartLoading()
        }
        fun setOnStartLoadingListener(onStartLoadingListener: OnStartLoadingListener){
            this.onStartLoadingListener = onStartLoadingListener
        }

        override fun loadInBackground(): JSONObject? {
            if (bundle == null) {
                return JSONObject()
            }
            val urlAsStrung = bundle.getString("url")
            var url: URL? = null
            try {
                url = URL(urlAsStrung)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            var result: JSONObject? = null

            if (url == null) {
                return null
            }
            var connection: HttpURLConnection? = null
            try {
                connection = url.openConnection() as HttpURLConnection
                val inputStream: InputStream = connection.inputStream
                val inputStreamReader: InputStreamReader? = if (inputStream != null) InputStreamReader(inputStream) else null
                val reader: BufferedReader? = if (inputStreamReader != null) BufferedReader(inputStreamReader) else null
                val builder = StringBuilder()
                var line = reader?.readLine()
                while (line != null) {
                    builder.append(line)
                    line = reader?.readLine()

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

        override fun onStartLoading() {
            super.onStartLoading()
            if(onStartLoadingListener != null){
                onStartLoadingListener.onStartLoading()
            }
            forceLoad()
        }
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


     class JSONLoadTask : AsyncTask<URL, Void, JSONObject>() {
        override fun doInBackground(vararg params: URL?): JSONObject? {
            var result: JSONObject? = null

            if (params == null || params.size == 0) {
                return result
            }
            var connection: HttpURLConnection? = null
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
