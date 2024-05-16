package com.example.connectnodejs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.connectnodejs.model.Genre
import com.example.connectnodejs.R
import com.example.connectnodejs.databinding.GenreViewHomeBinding

class HomeGenreAdapter(var genreList: MutableList<Genre>) :
    RecyclerView.Adapter<HomeGenreAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: GenreViewHomeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            GenreViewHomeBinding.inflate(
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
        if (genre != null) {
            Glide.with(holder.itemView.context)
                .load(genre.image)
                .placeholder(R.drawable.song_circle)
                .error(R.drawable.icon_shake_hand)
                .into(holder.binding.genreImg)

            holder.binding.genreName.text = genre.name
        }
    }

}