package com.fang.scroll

import android.util.Log
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * @author fangkw on 2020-10-23
 **/
class PagedListFetcher(private val recyclerView: RecyclerView,
                       private val lifecycleOwner: LifecycleOwner,
                       private val loadPage : (page : Int, hasMore : MoreCallback) -> Unit) {

    var page : Int = 0
    private var hasMore : Boolean = true
    private var isLoading : Boolean = false
    private var fetchJob : Job? = null

    init {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                fetchNextPage(recyclerView.layoutManager)
            }
        })
    }

    private fun fetchPage(page: Int) {
        if (isLoading) {
            return
        }

        // cancel
        fetchJob?.cancel()

        isLoading = true

        val exceptionHandler = CoroutineExceptionHandler{ _, _ ->
            Log.e("fang", "xxx")
        }
        lifecycleOwner.lifecycleScope.launch(exceptionHandler) {
            loadPage(page) {
                isLoading = false
                hasMore = it

                if (it) {
                    this@PagedListFetcher.page = page
                }
            }
        }
    }

    fun fetchNextPage(layoutManager : RecyclerView.LayoutManager?) {
        if (!hasMore) {
            return
        }
        when(layoutManager){
            is LinearLayoutManager -> {
                recyclerView.adapter?.itemCount?.let {
                    val count = it - 1
                    if (layoutManager.findLastVisibleItemPosition() >= count) {
                        fetchNextPage()
                    }
                }
            }
        }
    }

    private fun fetchNextPage() {
        if (!isLoading) {
            fetchPage(page + 1)
        }
    }
}