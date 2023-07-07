package dev.haqim.moviesapp.data.remote.response

import com.google.gson.annotations.SerializedName
import dev.haqim.moviesapp.domain.model.Genre

data class GenreListResponse(

	@field:SerializedName("genres")
	val genres: List<Item>
){
	data class Item(

		@field:SerializedName("name")
		val name: String,

		@field:SerializedName("id")
		val id: Int
	)
}

fun List<GenreListResponse.Item>.toModel(): List<Genre>{
	return this.map {
		Genre(
			id = it.id,
			name = it.name
		)
	}
}



