package com.example.mymovis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovis.R
import com.example.mymovis.data.Movie
import com.squareup.picasso.Picasso

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies: MutableList<Movie> = mutableListOf()
    private var onPosterClickListener: OnPosterClickListener? = null
    private var onReachEndListener: OnReachEndListener? = null

    fun setOnPosterClickListener(listener: OnPosterClickListener) {
        this.onPosterClickListener = listener
    }

    fun setOnReachEndListener(onReachEndListener: OnReachEndListener){
        this.onReachEndListener = onReachEndListener
    }
    interface OnPosterClickListener {
        fun onPosterClick(position: Int)
    }

    interface OnReachEndListener{
        fun onReachEnd()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        val holder = MovieViewHolder(view)
        holder.itemView.setOnClickListener {
            onPosterClickListener?.onPosterClick(holder.adapterPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        if (movies.size >= 20 &&  position >= movies.size -4 && onReachEndListener != null) {
            onReachEndListener?.onReachEnd()
        }
        val movie = movies[position]
        Picasso.get().load(movie.posterPath).into(holder.imageViewSmallPoster)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setMovies(movies: MutableList<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    fun addMovies(movies: MutableList<Movie>) {
        movies?.let {
            this.movies.addAll(it)
            notifyDataSetChanged()
        }
    }

    fun getMovies(): MutableList<Movie> {
        return movies ?: mutableListOf()
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewSmallPoster: ImageView = itemView.findViewById(R.id.imageViewSmallPoster)
        init {
            itemView.setOnClickListener {
                onPosterClickListener?.onPosterClick(adapterPosition)
            }
        }
    }
}
