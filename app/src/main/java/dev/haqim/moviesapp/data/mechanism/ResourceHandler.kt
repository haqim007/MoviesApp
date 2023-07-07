package dev.haqim.moviesapp.data.mechanism

import kotlinx.coroutines.flow.Flow


fun <T> Resource<T>.handle(
    onSuccess: () -> Unit,
    onError: () -> Unit = {},
    onLoading: () -> Unit = {},
    onIdle: () -> Unit = {}
){
    when (this) {
        is Resource.Success -> onSuccess.invoke()
        is Resource.Error -> onError.invoke()
        is Resource.Loading -> onLoading.invoke()
        else -> onIdle.invoke()
    }
}

suspend fun <T> Flow<Resource<T>>.handleCollect(
    onSuccess: (resource: Resource<T>) -> Unit,
    onError: (resource: Resource<T>) -> Unit = {},
    onLoading: (resource: Resource<T>) -> Unit = {},
    onIdle: () -> Unit = {}
){
    this.collect{ it.handle({ onSuccess(it) }, { onError(it) }, { onLoading(it) }, onIdle) }
}
