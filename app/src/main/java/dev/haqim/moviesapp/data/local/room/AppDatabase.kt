package dev.haqim.moviesapp.data.local.room


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.haqim.moviesapp.data.local.entity.MovieListItemEntity
import dev.haqim.moviesapp.data.local.entity.MovieListItemRemoteKeys
import dev.haqim.moviesapp.data.local.room.dao.MovieListItemDao
import dev.haqim.moviesapp.data.local.room.dao.MovieListItemRemoteKeysDao

@Database(
    entities = [
        MovieListItemRemoteKeys::class, 
        MovieListItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    
    abstract fun movieListItemRemoteKeys(): MovieListItemRemoteKeysDao
    abstract fun movieListItem(): MovieListItemDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        @JvmStatic
        fun getInstance(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movie_app.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { 
                        INSTANCE = it
                    }
            }
        }
    }
}