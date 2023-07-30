package dev.haqim.moviesapp.data.repository

import dev.haqim.moviesapp.data.mechanism.NetworkBoundResource
import dev.haqim.moviesapp.data.mechanism.Resource
import dev.haqim.moviesapp.data.remote.RemoteDataSource
import dev.haqim.moviesapp.data.remote.response.GenreListResponse
import dev.haqim.moviesapp.data.remote.response.toModel
import dev.haqim.moviesapp.di.DispatcherIO
import dev.haqim.moviesapp.domain.model.Genre
import dev.haqim.moviesapp.domain.repository.IGenreRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    @DispatcherIO
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): IGenreRepository {

    override fun getGenres(): Flow<Resource<List<Genre>>> {
        return object : NetworkBoundResource<List<Genre>, GenreListResponse>(){
            override suspend fun requestFromRemote(): Result<GenreListResponse> {
                return remoteDataSource.getGenres()
            }

            override fun loadResult(data: GenreListResponse): Flow<List<Genre>> {
                return flowOf(data.genres.toModel())
            }
        }.asFlow().flowOn(dispatcher)
    }

}