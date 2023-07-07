package dev.haqim.moviesapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieListItem(
    val id: Int,
    val posterUrl: String,
    val originalTitle: String,
    val vote: Vote
): Parcelable
