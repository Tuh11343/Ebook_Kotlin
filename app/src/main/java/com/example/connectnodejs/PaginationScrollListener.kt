package com.example.connectnodejs

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(var linearLayoutManager: LinearLayoutManager): RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val totalItemCount = linearLayoutManager.itemCount
        val visibleItemCount = linearLayoutManager.childCount
        val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()

        if (firstVisibleItemPosition > 0 && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
            if(isLoading()||isLastPage()){
                return
            }else{
                loadMoreItem()
            }
        }
    }

    abstract fun loadMoreItem()
    abstract fun isLoading():Boolean
    abstract fun isLastPage():Boolean
    
}