package dev.haqim.moviesapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    val name: String,
    val isChecked: Boolean = false,
    val id: Int
) : Parcelable {
    enum class GenresOperator(val separator: String){
        AND(","),
        OR("|")
    }
}

fun List<Genre>.toStringGenres(separator: String = " | "): String {
    return this.joinToString(separator) { it.name }
}