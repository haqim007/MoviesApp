package dev.haqim.moviesapp.data.local.room.dao

import androidx.paging.PagingSource
import dev.haqim.moviesapp.data.local.entity.MovieListItemEntity
import dev.haqim.moviesapp.util.MemoryPagingSource

class FakeMovieListItemDao: MovieListItemDao {
    
    private var movieListItems = mutableListOf<MovieListItemEntity>()
    
    override suspend fun insertData(data: List<MovieListItemEntity>) {
        this.movieListItems.addAll(data)
    }

    override suspend fun clearAllData() {
        this.movieListItems.clear()
    }

    override suspend fun getDataById(id: Int): MovieListItemEntity? {
        return this.movieListItems.find { item -> item.id == id }
    }

    override fun getPagingData(): PagingSource<Int, MovieListItemEntity> {
        return MemoryPagingSource(movieListItems)
    }

    override suspend fun resetAndInsert(data: List<MovieListItemEntity>) {
        super.resetAndInsert(data)
    }
}