package com.example.connectnodejs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.connectnodejs.R
import com.example.connectnodejs.databinding.SearchBookViewBinding
import com.example.connectnodejs.databinding.LoadingBinding
import com.example.connectnodejs.listener.SearchBookListener
import com.example.connectnodejs.model.Book

class SearchBookViewAdapter(var bookList: MutableList<Book>,var listener:SearchBookListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_BOOK = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    var isLoadingAdd = false

    inner class MyViewHolder(val binding: SearchBookViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class LoadingViewHolder(val binding: LoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LOADING -> LoadingViewHolder(LoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> MyViewHolder(SearchBookViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.itemViewType == VIEW_TYPE_BOOK){
            val book=bookList[holder.absoluteAdapterPosition]
            if(book!=null) {
                holder as MyViewHolder
                Glide.with(holder.itemView.context)
                    .load(book.image)
                    .placeholder(R.drawable.song_circle)
                    .error(R.drawable.song_circle)
                    .into(holder.binding.bookImg)

                holder.binding.bookName.text=book.name
                holder.binding.view.setOnClickListener{
                    listener.onBookClick(book)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(bookList!=null && position == bookList.size-1&&isLoadingAdd){
            return VIEW_TYPE_LOADING
        }
        return VIEW_TYPE_BOOK
    }

    fun addMoreBook(bookList:MutableList<Book>){
        this.bookList.addAll(bookList)
        notifyItemRangeChanged(this.bookList.size,bookList.size)
    }

    fun addFooterLoading(){
        isLoadingAdd=true
        bookList.add(Book())
        notifyItemInserted(bookList.size)
    }

    fun removeFooterLoading(){
        if(bookList[bookList.size-1]!=null){
            bookList.removeAt(bookList.size-1)
            notifyItemRemoved(bookList.size)
            isLoadingAdd=false
        }
    }

}