package dev.haqim.moviesapp.ui.movielist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dev.haqim.moviesapp.R
import dev.haqim.moviesapp.databinding.ItemMovieBinding
import dev.haqim.moviesapp.domain.model.MovieListItem


class MovieListAdapter(private val listener: MovieListAdapterListener):
    PagingDataAdapter<MovieListItem, RecyclerView.ViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.onCreate(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = getItem(position)
        (holder as ViewHolder).onBind(movie, listener)
    }

    class ViewHolder private constructor(private val binding: ItemMovieBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(movie: MovieListItem?, listener: MovieListAdapterListener) {

            movie?.let {
                Glide.with(itemView.context).load(it.posterUrl)
                    .placeholder(R.drawable.outline_downloading_24)
                    .transform(CenterInside(), RoundedCorners(24))
                    .into(binding.ivPoster)
                binding.tvRating.text = it.vote.rating
                binding.root.setOnClickListener {
                    listener.onClick(movie)
                }

            }
        }

        companion object{
            fun onCreate(parent: ViewGroup): ViewHolder {
                val itemView =
                    ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(itemView)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieListItem>() {

            override fun areItemsTheSame(oldItem: MovieListItem, newItem: MovieListItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieListItem, newItem: MovieListItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}

interface MovieListAdapterListener{
    fun onClick(movie: MovieListItem)
}