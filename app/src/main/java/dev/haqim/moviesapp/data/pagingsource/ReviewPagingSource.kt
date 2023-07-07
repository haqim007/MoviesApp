package dev.haqim.moviesapp.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.haqim.moviesapp.data.remote.RemoteDataSource
import dev.haqim.moviesapp.data.remote.response.toModel
import dev.haqim.moviesapp.data.util.DEFAULT_PAGE_SIZE
import dev.haqim.moviesapp.domain.model.ReviewItem
import retrofit2.HttpException
import java.io.IOException


private const val REVIEW_STARTING_PAGE_INDEX = 1
class ReviewPagingSource(
    private val remoteDataSource: RemoteDataSource,
    private val movieId: Int
): PagingSource<Int, ReviewItem>(){

    override fun getRefreshKey(state: PagingState<Int, ReviewItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReviewItem> {
        val position = params.key ?: REVIEW_STARTING_PAGE_INDEX
        return try {
            val response = remoteDataSource.getMovieReviews(movieId, position)
            val reviews = response.getOrThrow().toModel()
            val nextKey = if(reviews.isEmpty()){
                null
            }else{
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / DEFAULT_PAGE_SIZE)
            }
            LoadResult.Page(
                data = reviews,
                prevKey = if (position == REVIEW_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        }catch (exception: IOException){
            LoadResult.Error(exception)
        }catch (exception: HttpException) {
            return LoadResult.Error(exception)
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}