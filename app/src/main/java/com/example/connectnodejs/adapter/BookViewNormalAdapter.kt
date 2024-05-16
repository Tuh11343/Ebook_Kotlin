package com.example.connectnodejs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.connectnodejs.R
import com.example.connectnodejs.databinding.BookViewNormalBinding
import com.example.connectnodejs.listener.IBookListener
import com.example.connectnodejs.model.Book

class BookViewNormalAdapter(var bookList: MutableList<Book>, var mListener: IBookListener) :
    RecyclerView.Adapter<BookViewNormalAdapter.BookViewHolder>() {

    inner class BookViewHolder(val binding: BookViewNormalBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(
            BookViewNormalBinding.inflate(
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
        val book = bookList[holder.absoluteAdapterPosition]
        if (book != null) {
            Glide.with(holder.itemView.context)
                .load(book.image)
                .placeholder(R.drawable.song_circle)
                .error(R.drawable.song_circle)
                .centerCrop()
                .into(holder.binding.bookImg)

            if(book.book_type==Book.BookType.NORMAL){
                holder.binding.bookType.text="Miễn phí"
            }else if(book.book_type==Book.BookType.PREMIUM){
                holder.binding.bookType.text="Giới hạn"
            }

            holder.binding.bookImg.setOnClickListener {
                val shrinkAnimation =
                    AnimationUtils.loadAnimation(holder.itemView.context, R.anim.shrink_effect)
                shrinkAnimation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        mListener.onBookClick(book)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }
                })

                holder.binding.bookLayout.startAnimation(shrinkAnimation)
            }
        }
    }

}