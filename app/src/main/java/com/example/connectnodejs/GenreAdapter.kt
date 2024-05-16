/*
package com.example.connectnodejs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.connectnodejs.databinding.LoadingBinding
import com.example.connectnodejs.model.Genre

class GenreAdapter(
    var genreList: MutableList<Genre>,
    var length:Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        private const val VIEW_TYPE_GENRE = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    var isLoadingAdd=false

    inner class GenreViewHolder(val binding: GenreBinding) : RecyclerView.ViewHolder(binding.root)
    inner class LoadingViewHolder(val binding: LoadingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LOADING -> LoadingViewHolder(LoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> GenreViewHolder(GenreBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(genreList!=null && position == genreList.size-1&&isLoadingAdd){
            return VIEW_TYPE_LOADING
        }
        return VIEW_TYPE_GENRE
    }

    override fun getItemCount(): Int {
        return genreList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.itemViewType == VIEW_TYPE_GENRE){
            val genre=genreList[position]
            if(genre!=null) {
                val genreHolder = holder as GenreViewHolder
                genreHolder.binding.id.text = genre.id.toString()
                genreHolder.binding.name.text = genre.name

            }
        }
    }

    fun isLastPage():Boolean{
        if(genreList.size==length){
            return true
        }
        return false
    }

    fun updateUI(){
        notifyDataSetChanged()
    }

    fun addFooterLoading(){
        isLoadingAdd=true
        genreList.add(Genre())
        notifyDataSetChanged()
    }

    fun removeFooterLoading(){
        if(genreList[genreList.size-1]!=null){
            genreList.removeAt(genreList.size-1)
            notifyDataSetChanged()
            isLoadingAdd=false
        }
    }
}
*/
