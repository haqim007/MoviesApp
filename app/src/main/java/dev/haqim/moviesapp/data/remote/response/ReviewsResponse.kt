package dev.haqim.moviesapp.data.remote.response

import com.google.gson.annotations.SerializedName
import dev.haqim.moviesapp.BuildConfig
import dev.haqim.moviesapp.data.util.DEFAULT_DATETIME_FORMAT
import dev.haqim.moviesapp.data.util.formatDate
import dev.haqim.moviesapp.domain.model.ReviewItem

data class ReviewsResponse(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("total_pages")
	val totalPages: Int,

	@field:SerializedName("results")
	val results: List<ReviewItemResponse>,

	@field:SerializedName("total_results")
	val totalResults: Int
){

	data class ReviewItemResponse(

		@field:SerializedName("author_details")
		val authorDetails: AuthorDetails,

		@field:SerializedName("updated_at")
		val updatedAt: String,

		@field:SerializedName("author")
		val author: String,

		@field:SerializedName("created_at")
		val createdAt: String,

		@field:SerializedName("id")
		val id: String,

		@field:SerializedName("content")
		val content: String,

		@field:SerializedName("url")
		val url: String
	){
		data class AuthorDetails(

			@field:SerializedName("avatar_path")
			val avatarPath: String?,

			@field:SerializedName("name")
			val name: String,

			@field:SerializedName("rating")
			val rating: Int,

			@field:SerializedName("username")
			val username: String
		)
	}


}

fun ReviewsResponse.toModel(): List<ReviewItem>{
	return this.results.map {
		ReviewItem(
			id = it.id,
			author = ReviewItem.Author(
				name = it.author,
				avatarUrl = BuildConfig.BASE_IMAGE_URL + it.authorDetails.avatarPath,
				rating = it.authorDetails.rating
			),
			content = it.content,
			createdAt = it.createdAt.formatDate(DEFAULT_DATETIME_FORMAT),
			updatedAt = it.updatedAt.formatDate(DEFAULT_DATETIME_FORMAT)
		)
	}
}

