package dev.haqim.moviesapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.haqim.moviesapp.data.local.entity.MOVIE_LIST_ITEM_REMOTE_KEYS
import dev.haqim.moviesapp.data.local.entity.MovieListItemRemoteKeys

@Dao
interface MovieListItemRemoteKeysDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey:List<MovieListItemRemoteKeys>)

    @Query("SELECT * FROM $MOVIE_LIST_ITEM_REMOTE_KEYS where id = :id")
    suspend fun getRemoteKeyById(id: Int): MovieListItemRemoteKeys?

    @Query("DELETE FROM $MOVIE_LIST_ITEM_REMOTE_KEYS")
    suspend fun clearRemoteKeys()
}