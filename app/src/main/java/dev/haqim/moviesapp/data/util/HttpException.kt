package dev.haqim.moviesapp.data.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.haqim.moviesapp.data.remote.response.ErrorResponse
import retrofit2.HttpException
import java.lang.Exception

fun HttpException.toErrorMessage(): String {
    val errorJson = this.response()?.errorBody()?.string()
    val data = object : TypeToken<ErrorResponse>() {}.type
    val response =  try {
        Gson().fromJson<ErrorResponse>(errorJson, data)
    }catch (e: Exception){
        null
    }

    return response?.statusMessage ?: "Error"
}