package com.example.connectnodejs.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.connectnodejs.R
import com.example.connectnodejs.databinding.BookViewBinding
import com.example.connectnodejs.listener.IMultiBookListener
import com.example.connectnodejs.model.Book

class BookWithMultiAdapter(
    var bookList: MutableList<Book>,
    var mListener: IMultiBookListener
) :
    RecyclerView.Adapter<BookWithMultiAdapter.BookViewHolder>() {

    inner class BookViewHolder(val binding: BookViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(
            BookViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return bookList.size / 2
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {

        val bookPosition:Int
        if(holder.absoluteAdapterPosition==0)
            bookPosition=holder.absoluteAdapterPosition
        else if(holder.absoluteAdapterPosition==1)
            bookPosition=holder.absoluteAdapterPosition+1
        else
            bookPosition=holder.absoluteAdapterPosition+2

        val book = bookList[bookPosition]
        var book3 = Book()
        if (bookPosition + 1 < bookList.size) {
            book3 = bookList[bookPosition + 1]
        }

        if (book != null) {
            Glide.with(holder.itemView.context)
                .load(book.image)
                .placeholder(R.drawable.song_circle)
                .error(R.drawable.song_circle)
                .centerCrop()
                .into(holder.binding.bookImg)

            if (book.book_type == Book.BookType.NORMAL) {
                holder.binding.bookType.text = "Miễn phí"
            } else if (book.book_type == Book.BookType.PREMIUM) {
                holder.binding.bookType.text = "Giới hạn"
            }

            holder.binding.bookImg.setOnClickListener {
                val shrinkAnimation =
                    AnimationUtils.loadAnimation(holder.itemView.context, R.anim.shrink_effect)
                shrinkAnimation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        Log.i("Nothing","Position:${bookPosition}")
                        mListener.onFirstBookClick(book)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }
                })

                holder.binding.cardView.startAnimation(shrinkAnimation)
            }

        }

        if (book3 != null) {
            Glide.with(holder.itemView.context)
                .load(book3.image)
                .placeholder(R.drawable.song_circle)
                .error(R.drawable.song_circle)
                .into(holder.binding.bookImg3)

            if (book3.book_type == Book.BookType.NORMAL) {
                holder.binding.bookType3.text = "Miễn phí"
            } else if (book3.book_type == Book.BookType.PREMIUM) {
                holder.binding.bookType3.text = "Giới hạn"
            }


            holder.binding.bookImg3.setOnClickListener {
                val shrinkAnimation =
                    AnimationUtils.loadAnimation(holder.itemView.context, R.anim.shrink_effect)
                shrinkAnimation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        Log.i("Nothing","Position:${bookPosition+1}")
                        mListener.onSecondBookClick(book3)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }
                })

                holder.binding.cardView3.startAnimation(shrinkAnimation)
            }
        }


    }


}