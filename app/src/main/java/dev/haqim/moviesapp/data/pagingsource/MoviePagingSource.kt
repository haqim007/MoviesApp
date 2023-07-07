package dev.haqim.moviesapp.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.haqim.moviesapp.data.remote.RemoteDataSource
import dev.haqim.moviesapp.data.util.DEFAULT_PAGE_SIZE
import dev.haqim.moviesapp.domain.model.Genre
import dev.haqim.moviesapp.domain.model.MovieListItem
import retrofit2.HttpException
import java.io.IOException

private const val MOVIE_STARTING_PAGE_INDEX = 1
class MoviePagingSource(
    private val remoteDataSource: RemoteDataSource,
    private val genres: List<Genre> = listOf(),
    private val genresOperator: Genre.GenresOperator? = null
): PagingSource<Int, MovieListItem>(){

    override fun getRefreshKey(state: PagingState<Int, MovieListItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieListItem> {
        val position = params.key ?: MOVIE_STARTING_PAGE_INDEX
        return try {
            val response = remoteDataSource.getMovies(position, genres, genresOperator)
            val movies = response.getOrThrow().toModel()
            val nextKey = if(movies.isEmpty()){
                null
            }else{
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / DEFAULT_PAGE_SIZE)
            }
            LoadResult.Page(
                data = movies,
                prevKey = if (position == MOVIE_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        }catch (exception: IOException){
            LoadResult.Error(exception)
        }catch (exception: HttpException) {
            return LoadResult.Error(exception)
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
        catch (e: Throwable){
            return LoadResult.Error(e)
        }
    }
}