package dev.haqim.moviesapp.data.local.room.dao

import dev.haqim.moviesapp.data.local.entity.MovieListItemRemoteKeys

class FakeMovieRemoteKeysDao: MovieListItemRemoteKeysDao {
    
    private var remoteKeys: MutableList<MovieListItemRemoteKeys> = mutableListOf()
    
    override suspend fun insertAll(remoteKey: List<MovieListItemRemoteKeys>) {
        remoteKeys.addAll(remoteKey)
    }

    override suspend fun getRemoteKeyById(id: Int): MovieListItemRemoteKeys? {
        return remoteKeys.find { key -> key.id == id }
    }

    override suspend fun clearRemoteKeys() {
        remoteKeys.clear()
    }
}