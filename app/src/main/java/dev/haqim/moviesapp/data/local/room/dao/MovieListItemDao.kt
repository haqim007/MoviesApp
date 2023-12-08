package dev.haqim.moviesapp.data.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.haqim.moviesapp.data.local.entity.MOVIE_ITEM_LIST_TABLE
import dev.haqim.moviesapp.data.local.entity.MovieListItemEntity


@Dao
interface MovieListItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: List<MovieListItemEntity>)

    @Query("DELETE FROM $MOVIE_ITEM_LIST_TABLE")
    suspend fun clearAllData()
    
    @Query("SELECT * FROM $MOVIE_ITEM_LIST_TABLE WHERE id = :id")
    suspend fun getDataById(id: Int): MovieListItemEntity?

    @Query("SELECT * FROM $MOVIE_ITEM_LIST_TABLE")
    fun getPagingData(): PagingSource<Int, MovieListItemEntity>
    
    @Transaction
    suspend fun resetAndInsert(data: List<MovieListItemEntity>){
        clearAllData()
        insertData(data)
    }
}