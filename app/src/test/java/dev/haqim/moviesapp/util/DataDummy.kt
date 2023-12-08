package dev.haqim.moviesapp.util

import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import dev.haqim.moviesapp.data.local.entity.MovieListItemRemoteKeys
import dev.haqim.moviesapp.data.remote.response.MovieListResponse
import dev.haqim.moviesapp.data.remote.response.toEntity
import java.io.File
import java.io.InputStream


@VisibleForTesting
object DataDummy {
    
   
//    fun movieListItemResponseJson() = JsonConverter.readStringFromFile("movie_list_items_response.json")
    fun movieListItemResponseJson(): String? {
        return javaClass.classLoader?.getResource("movie_list_items_response.json")?.readText()
    }
    
    fun movieListResponse() = Gson().fromJson(movieListItemResponseJson(), MovieListResponse::class.java)
    
    fun movieListItemEntities() = movieListResponse().results.toEntity()
    
    val movieListItemRemoteKeys = listOf(
        MovieListItemRemoteKeys(1, null, 5),
        MovieListItemRemoteKeys(2, null, 2),
        MovieListItemRemoteKeys(3, null, 3),
        MovieListItemRemoteKeys(4, null, 4)
    )
    
}