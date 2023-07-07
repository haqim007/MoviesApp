package dev.haqim.moviesapp.domain.usecase

import dev.haqim.moviesapp.data.mechanism.Resource
import dev.haqim.moviesapp.data.repository.GenreRepository
import dev.haqim.moviesapp.di.DispatcherDefault
import dev.haqim.moviesapp.domain.model.Genre
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GenreInteractor @Inject constructor (
    private val repo: GenreRepository,
    @DispatcherDefault
    private val dispatcher: CoroutineDispatcher
): GenreUseCase {
    override fun getGenres(): Flow<Resource<List<Genre>>> {
        return repo.getGenres()
    }

    override suspend fun toggleCheckedGenre(
        selectedGenre: Genre,
        genres: Resource<List<Genre>>
    ): Resource<List<Genre>> {
        return withContext(dispatcher){
            // modify Resource.Success data only
            if(genres is Resource.Success){
                val genresData = genres.data
                val newGenresData = genresData?.map { genre ->
                    if(selectedGenre == genre){
                       selectedGenre.copy(isChecked = !selectedGenre.isChecked)
                    }else{
                        genre
                    }
                } ?: listOf()
                Resource.Success(newGenresData)
            }else{
                genres
            }
        }
    }
}