package com.example.mymovis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mymovis.data.FavouriteMovie
import com.example.mymovis.data.MainViewModel
import com.example.mymovis.data.Movie
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.viewModelScope


class DetailActivity : AppCompatActivity() {

    private lateinit var imageViewBigPoster: ImageView
    private lateinit var textViewLabelTitle: TextView
    private lateinit var textViewTitle: TextView
    private lateinit var textViewOriginalTitle: TextView
    private lateinit var textViewOrogonalTitle: TextView
    private lateinit var textViewRaiting: TextView
    private lateinit var textView_Raiting: TextView
    private lateinit var textViewRelisDay: TextView
    private lateinit var textViewReleaseDate: TextView
    private lateinit var textViewLabelDescription: TextView
    private lateinit var textViewOverView: TextView
    private lateinit var imageViewAddToFaivory : ImageView

    private var favouriteMovie: FavouriteMovie? = null

    private var id = 0

    private lateinit var viewModel: MainViewModel
    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        imageViewBigPoster = findViewById(R.id.imageViewBigPoster)
        textViewLabelTitle = findViewById(R.id.textViewLabelTitle)
        textViewTitle = findViewById(R.id.textViewTitle)
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle)
        textViewOrogonalTitle = findViewById(R.id.textViewOrogonalTitle)
        textViewRaiting = findViewById(R.id.textViewRaiting)
        textView_Raiting = findViewById(R.id.textView_Raiting)
        textViewRelisDay = findViewById(R.id.textViewRelisDay)
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate)
        textViewLabelDescription = findViewById(R.id.textViewLabelDescription)
        textViewOverView = findViewById(R.id.textViewOverView)
        imageViewAddToFaivory = findViewById(R.id.imageViewAddToFaivory)

        val intent = intent
        if (intent != null && intent.hasExtra("id")) {
            id = intent.getIntExtra("id", -1)
        } else {
            finish()
        }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        lifecycleScope.launch {
            movie = withContext(Dispatchers.IO) { viewModel.getMovieById(id) }
            Picasso.get().load(movie.bigPosterPath).into(imageViewBigPoster)
            textViewTitle.text = movie.title
            textViewOrogonalTitle.text = movie.originalTitle
            textViewOverView.text = movie.overView
            textViewReleaseDate.text = movie.releaseDate
            textView_Raiting.text = movie.voteAverage.toString()

            setFavourite()
        }

        imageViewAddToFaivory.setOnClickListener {
            viewModel.viewModelScope.launch {
                if (favouriteMovie == null) {
                    viewModel.insertFavouriteMovies(FavouriteMovie(movie))
                    Toast.makeText(this@DetailActivity, "Added to favorites", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.deleteFavouriteMovies(favouriteMovie!!)
                    Toast.makeText(this@DetailActivity, "Removed from favorites", Toast.LENGTH_SHORT).show()
                }
                setFavourite()
            }
        }
    }
    private suspend fun setFavourite(){
        favouriteMovie = viewModel.getFavouriteMovieById(id)
        if(favouriteMovie == null){
            imageViewAddToFaivory.setImageResource(R.drawable.favourite_add_to)
        }else {
            imageViewAddToFaivory.setImageResource(R.drawable.favourite_remove)
        }
    }
}
