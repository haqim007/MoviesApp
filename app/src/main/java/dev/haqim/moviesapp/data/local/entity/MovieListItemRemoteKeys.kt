package dev.haqim.moviesapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val MOVIE_LIST_ITEM_REMOTE_KEYS = "movie_list_item_remote_keys"

/**
 * To store information of fetched latest page
 *
 * @property id
 * @property prevKey
 * @property nextKey
 * @constructor Create empty Remote keys
 */
@Entity(tableName = MOVIE_LIST_ITEM_REMOTE_KEYS)
data class MovieListItemRemoteKeys(
    @PrimaryKey val id: Int,
    val prevKey: Int?,
    val nextKey: Int?
)