package dev.haqim.moviesapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val genres: List<Genre>,
    val title: String,
    val plotSummary: String,
    val posterUrl: String,
    val status: String,
    val budget: String,
    val revenue: String,
    val releaseDate: String,
    val vote: Vote,
    val trailerKey: String?
) : Parcelable {
    val votes: String
        get() = "(${vote.countString} Votes)"
}
