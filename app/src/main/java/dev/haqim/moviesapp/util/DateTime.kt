package dev.haqim.moviesapp.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.format(pattern: String = "yyyy-MM-dd HH:mm:ss"):  String?{
    val formatter = DateTimeFormatter.ofPattern(pattern)

    return LocalDateTime.now().format(formatter)
}