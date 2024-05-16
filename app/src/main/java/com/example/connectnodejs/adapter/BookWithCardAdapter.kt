package com.example.connectnodejs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.connectnodejs.R
import com.example.connectnodejs.databinding.BookviewWithcardBinding
import com.example.connectnodejs.model.Book

class BookWithCardAdapter(private var bookList: MutableList<Book>) :
    RecyclerView.Adapter<BookWithCardAdapter.BookViewHolder>() {

    inner class BookViewHolder(val binding: BookviewWithcardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookViewHolder {
        return BookViewHolder(
            BookviewWithcardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book=bookList[holder.absoluteAdapterPosition]
        if(book!=null){
            Glide.with(holder.itemView.context)
                .load(book.image)
                .placeholder(R.drawable.song_circle)
                .error(R.drawable.song_circle)
                .into(holder.binding.bookImg)

            holder.binding.genreName.text="Kỹ năng"
        }
    }

}