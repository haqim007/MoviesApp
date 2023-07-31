package dev.haqim.moviesapp.data.local.datasource

import androidx.room.withTransaction
import dev.haqim.moviesapp.data.local.entity.MovieListItemEntity
import dev.haqim.moviesapp.data.local.entity.MovieListItemRemoteKeys
import dev.haqim.moviesapp.data.local.room.AppDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieLocalDatasource @Inject constructor(
    private val database: AppDatabase
) {
    private val remoteKeysDao = database.movieListItemRemoteKeys()
    private val movieListItemDao = database.movieListItem()
    
    fun getPaging() = movieListItemDao.getPagingData()

    suspend fun clearRemoteKeys() = remoteKeysDao.clearRemoteKeys()

    suspend fun insertRemoteKeys(keys: List<MovieListItemRemoteKeys>) = remoteKeysDao.insertAll(keys)

    suspend fun getRemoteKeysById(id: Int) = remoteKeysDao.getRemoteKeyById(id)

    suspend fun getDataById(id: Int) = movieListItemDao.getDataById(id)

    suspend fun clearAllData() = movieListItemDao.clearAllData()

    suspend fun insertAllData(movieItems: List<MovieListItemEntity>) = movieListItemDao.insertData(movieItems)

    suspend fun insertKeysAndData(
        keys: List<MovieListItemRemoteKeys>,
        movieItems: List<MovieListItemEntity>,
        isRefresh: Boolean = false
    ){
        database.withTransaction {
            if (isRefresh) {
                clearRemoteKeys()
                clearAllData()
            }
            insertRemoteKeys(keys)
            insertAllData(movieItems)
        }
    }


//    companion object{
//        private var INSTANCE: MovieLocalDatasource? = null
//
//        fun getInstance(database: AppDatabase) = INSTANCE ?: synchronized(this){
//            INSTANCE ?: MovieLocalDatasource(database)
//        }
//    }
    
}