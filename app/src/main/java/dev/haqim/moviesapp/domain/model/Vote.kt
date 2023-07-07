package dev.haqim.moviesapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vote(
    val count: Int,
    val average: Double,
) : Parcelable {
    val countString: String
        get() {
            return if(count in 1000..999999) {
                "${count/1000}K"
            }else if(count >= 1000000){
                "${count/1000000}M"
            }else{
                count.toString()
            }
        }
    val rating: String
        get() = average.toString()
    val ratingProgressStar: Float
        get() = (average / 10.0 * 5).toFloat()
}