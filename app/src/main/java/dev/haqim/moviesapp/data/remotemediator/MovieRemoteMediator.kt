package dev.haqim.moviesapp.data.remotemediator


import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import dev.haqim.moviesapp.data.local.datasource.MovieLocalDatasource
import dev.haqim.moviesapp.data.local.entity.MovieListItemEntity
import dev.haqim.moviesapp.data.local.entity.MovieListItemRemoteKeys
import dev.haqim.moviesapp.data.remote.RemoteDataSource
import dev.haqim.moviesapp.data.remote.response.toEntity
import dev.haqim.moviesapp.domain.model.Genre


@OptIn(ExperimentalPagingApi::class)
class MovieListRemoteMediator(
    private val localDataSource: MovieLocalDatasource,
    private val remoteDataSource: RemoteDataSource,
    private val genres: List<Genre> = listOf(),
    private val genresOperator: Genre.GenresOperator? = null

): RemoteMediator<Int, MovieListItemEntity>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, MovieListItemEntity>): MediatorResult {
        
        val page = when(loadType){
            LoadType.REFRESH -> {
                val remoteKeys: MovieListItemRemoteKeys? = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys: MovieListItemRemoteKeys? = getRemoteKeyForFirstItem(state)
                val prevKey: Int = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys: MovieListItemRemoteKeys? = getRemoteKeyForLastItem(state)
                val nextKey: Int = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        val result = try {
            val response = remoteDataSource.getMovies(page, genres, genresOperator)
            val endOfPaginationReached: Boolean = response.getOrNull()?.results?.isEmpty() ?: true

            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (endOfPaginationReached) null else page + 1
            val resultData = response.getOrNull()?.results ?: listOf()
            
            val keys: List<MovieListItemRemoteKeys> = resultData.map { 
                MovieListItemRemoteKeys(
                    id = it.id,
                    prevKey, nextKey
                )
            }

            if(!endOfPaginationReached){
                localDataSource.insertKeysAndData(
                    keys,
                    resultData.toEntity(),
                    loadType == LoadType.REFRESH
                )
            }
            
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        }catch (e: Exception){
            MediatorResult.Error(e)
        }

        return result

    }


    // TODO: Adjust with the data
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MovieListItemEntity>): MovieListItemRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                localDataSource.getRemoteKeysById(id)
            }
        }
    }

    // TODO: Adjust with the data
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieListItemEntity>): MovieListItemRemoteKeys?{
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { data ->        
                localDataSource.getRemoteKeysById(data.id)
            }
    }

    // TODO: Adjust with the data
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieListItemEntity>): MovieListItemRemoteKeys?{
        return state.pages.lastOrNull{it.data.isNotEmpty()}?.data?.lastOrNull()?.let { data ->
            localDataSource.getRemoteKeysById(data.id)
        }
    }

     companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}