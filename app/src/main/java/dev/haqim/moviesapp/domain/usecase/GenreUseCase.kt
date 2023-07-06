package dev.haqim.moviesapp.domain.usecase

import dev.haqim.moviesapp.domain.model.Genre

interface GenreUseCase {
    fun getGenres(): List<Genre>
}