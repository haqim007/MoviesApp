package dev.haqim.moviesapp.domain.repository

import dev.haqim.moviesapp.data.mechanism.Resource
import dev.haqim.moviesapp.domain.model.Genre
import kotlinx.coroutines.flow.Flow


interface IGenreRepository{

    fun getGenres(): Flow<Resource<List<Genre>>>

}