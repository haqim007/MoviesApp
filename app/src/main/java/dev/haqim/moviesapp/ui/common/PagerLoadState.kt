package dev.haqim.moviesapp.ui.common

import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.haqim.moviesapp.R
import dev.haqim.moviesapp.databinding.ErrorViewBinding
import kotlinx.coroutines.launch

fun <ItemDataType : Any> PagingDataAdapter<ItemDataType, RecyclerView.ViewHolder>.pagerLoadState(
    scope: LifecycleCoroutineScope,
    listView: RecyclerView,
    context: Context,
    onTryAgain: () -> Unit = {},
    errorMessage: String? = null,
    loader: View? = null,
    errorView: ErrorViewBinding? = null,
){
    scope.launch {
        val adapter = this@pagerLoadState
        adapter.loadStateFlow.collect { loadState ->
            val isListEmpty =
                loadState.refresh is LoadState.NotLoading &&
                        adapter.itemCount == 0 &&
                        loadState.prepend.endOfPaginationReached

            errorView?.root?.isVisible = isListEmpty

            if(isListEmpty){
               errorView?.tvErrorMessage?.text = context.getString(R.string.list_is_empty)
            }

            if(loader != null){
                listView.isVisible = !isListEmpty && loadState.refresh is LoadState.NotLoading
            }else{
                listView.isVisible = !isListEmpty
            }

            val isLoading = loadState.source.refresh is LoadState.Loading
            val isNotLoading = loadState.source.refresh is LoadState.NotLoading

            loader?.isVisible = isLoading

            if(isNotLoading){
                listView.scrollToPosition(0)
            }

            errorView?.btnTryAgain?.setOnClickListener {
                adapter.retry()
                onTryAgain()
            }

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
                ?: loadState.refresh as? LoadState.Error

            errorState?.let {
                errorView?.root?.isVisible = true
                errorView?.tvErrorMessage?.text = context.getString(R.string.an_error_occured)
            } ?: run{
                errorView?.root?.isVisible = false
                errorView?.tvErrorMessage?.text = errorMessage
                    ?: context.getString(R.string.an_error_occured)
            }
        }
    }
}
