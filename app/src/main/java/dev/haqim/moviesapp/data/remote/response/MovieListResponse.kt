package dev.haqim.moviesapp.data.remote.response

import com.google.gson.annotations.SerializedName
import dev.haqim.moviesapp.BuildConfig
import dev.haqim.moviesapp.data.local.entity.MovieListItemEntity
import dev.haqim.moviesapp.domain.model.MovieListItem
import dev.haqim.moviesapp.domain.model.Vote


data class MovieListResponse(

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("total_pages")
	val totalPages: Int,

	@field:SerializedName("results")
	val results: List<Item>,

	@field:SerializedName("total_results")
	val totalResults: Int
){

	data class Item(

		@field:SerializedName("overview")
		val overview: String,

		@field:SerializedName("original_language")
		val originalLanguage: String,

		@field:SerializedName("original_title")
		val originalTitle: String,

		@field:SerializedName("video")
		val video: Boolean,

		@field:SerializedName("title")
		val title: String,

		@field:SerializedName("genre_ids")
		val genreIds: List<Int>,

		@field:SerializedName("poster_path")
		val posterPath: String,

		@field:SerializedName("backdrop_path")
		val backdropPath: String,

		@field:SerializedName("release_date")
		val releaseDate: String,

		@field:SerializedName("popularity")
		val popularity: Double,

		@field:SerializedName("vote_average")
		val voteAverage: Double,

		@field:SerializedName("id")
		val id: Int,

		@field:SerializedName("adult")
		val adult: Boolean,

		@field:SerializedName("vote_count")
		val voteCount: Int
	)

	fun toModel(): List<MovieListItem>{
		return this.results.map {
			MovieListItem(
				id = it.id,
				posterUrl =  BuildConfig.BASE_IMAGE_URL + it.posterPath,
				originalTitle = it.originalTitle,
				vote = Vote(
					count = it.voteCount,
					average = it.voteAverage
				)
			)
		}
	}
}

fun List<MovieListResponse.Item>.toEntity(): List<MovieListItemEntity>{
	return this.map { 
		MovieListItemEntity(
			id = it.id,
			posterUrl =  BuildConfig.BASE_IMAGE_URL + it.posterPath,
			originalTitle = it.originalTitle,
			voteCount = it.voteCount,
			voteAverage = it.voteAverage
		)
	}
}

