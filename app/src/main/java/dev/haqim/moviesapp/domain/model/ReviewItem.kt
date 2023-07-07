package dev.haqim.moviesapp.domain.model


data class ReviewItem(
    val id: String,
    val author: Author,
    val content: String,
    val createdAt: String,
    val updatedAt: String
){
    data class Author(
        val name: String,
        val avatarUrl: String?,
        val rating: Int
    )
}
