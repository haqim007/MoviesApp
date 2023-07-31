package dev.haqim.moviesapp.data.util

import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

suspend fun <T> getResult(callback: suspend () -> T): Result<T> {
    return try {
        Result.success(callback())
    }
    catch (e: UnknownHostException){
        val message = e.message ?: ""
        if (message.contains("failed to connect")) {
            // Handle an unavailable network (e.g., airplane mode, Wi-Fi turned off)
            Result.failure(Throwable(message = "Unavailable network"))
        } else {
            // Handle an unknown host error (e.g., server name cannot be resolved)
            Result.failure(Throwable(message = "Unknown to resolve host"))
        }
        
    }
    catch (e: HttpException){
        Result.failure(Throwable(message = e.toErrorMessage()))
    }
    catch (e: ConnectException){
        Result.failure(Throwable(message = "Unable to connect"))
    }
    catch (e: Exception){
        Result.failure(e)
    }
}