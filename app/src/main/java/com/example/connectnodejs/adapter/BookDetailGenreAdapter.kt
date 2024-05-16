package com.example.connectnodejs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.connectnodejs.model.Genre
import com.example.connectnodejs.databinding.BookDetailGenreBinding

class BookDetailGenreAdapter(var genreList: MutableList<Genre>) :
    RecyclerView.Adapter<BookDetailGenreAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: BookDetailGenreBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BookDetailGenreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return genreList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = genreList[holder.absoluteAdapterPosition]

    }

}