package dev.haqim.moviesapp.ui.reviewlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dev.haqim.moviesapp.R
import dev.haqim.moviesapp.databinding.ItemReviewBinding
import dev.haqim.moviesapp.domain.model.ReviewItem


class ReviewListAdapter():
    PagingDataAdapter<ReviewItem, RecyclerView.ViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.onCreate(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val review = getItem(position)
        (holder as ViewHolder).onBind(review)
    }

    class ViewHolder private constructor(private val binding: ItemReviewBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(review: ReviewItem?) {

            review?.let {
                Glide.with(itemView.context).load(it.author.avatarUrl)
                    .placeholder(R.drawable.outline_downloading_24)
                    .transform(CenterInside(), RoundedCorners(24))
                    .into(binding.ivAvatar)
                binding.tvAuthor.text = it.author.name
                binding.tvComment.text = it.content
                binding.tvCommentDate.text = it.updatedAt

            }
        }

        companion object{
            fun onCreate(parent: ViewGroup): ViewHolder {
                val itemView =
                    ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(itemView)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ReviewItem>() {

            override fun areItemsTheSame(oldItem: ReviewItem, newItem: ReviewItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ReviewItem, newItem: ReviewItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
