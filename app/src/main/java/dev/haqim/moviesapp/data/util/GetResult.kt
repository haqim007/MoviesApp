package dev.haqim.moviesapp.data.util

import retrofit2.HttpException
import java.net.UnknownHostException

suspend fun <T> getResult(callback: suspend () -> T): Result<T> {
    return try {
        Result.success(callback())
    }
    catch (e: UnknownHostException){
        Result.failure(Throwable(message = "Invalid URL"))
    }
    catch (e: HttpException){
        Result.failure(Throwable(message = e.toErrorMessage()))
    }
    catch (e: Exception){
        Result.failure(e)
    }
}