package dev.haqim.moviesapp.domain.usecase

import dev.haqim.moviesapp.domain.model.Movie

interface MovieUseCase {
   fun getMovies(): List<Movie>
}