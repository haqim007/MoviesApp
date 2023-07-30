package dev.haqim.moviesapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.haqim.moviesapp.BuildConfig
import dev.haqim.moviesapp.domain.model.MovieListItem
import dev.haqim.moviesapp.domain.model.Vote
import dev.haqim.moviesapp.util.format
import java.time.LocalDateTime

const val MOVIE_ITEM_LIST_TABLE = "movie_list_item"
@Entity(MOVIE_ITEM_LIST_TABLE)
data class MovieListItemEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name="poster_url")
    val posterUrl: String,
    @ColumnInfo(name="original_title")
    val originalTitle: String,
    @ColumnInfo(name="vote_count")
    val voteCount: Int,
    @ColumnInfo(name="vote_average")
    val voteAverage: Double,
    @ColumnInfo(name="last_update")
    val lastUpdate: String? = LocalDateTime.now().format()
)

fun MovieListItemEntity.toModel(): MovieListItem{
    return MovieListItem(
        id = this.id,
        posterUrl = this.posterUrl,
        originalTitle = this.originalTitle,
        vote = Vote(
            count = this.voteCount,
            average = this.voteAverage
        )
    )
}

fun List<MovieListItemEntity>.toModel(): List<MovieListItem>{
    return this.map{it.toModel()}
}