package com.example.connectnodejs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.connectnodejs.databinding.GenreBottomSheetBinding
import com.example.connectnodejs.listener.FilterListener
import com.example.connectnodejs.model.Genre

class FilterBottomSheetAdapter(var genreList: MutableList<Genre>, var listener: FilterListener) :
    RecyclerView.Adapter<FilterBottomSheetAdapter.MyViewHolder>() {

    private var checkedPosition = -1

    inner class MyViewHolder(val binding: GenreBottomSheetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: Genre) {
            if(checkedPosition==-1){
                binding.checkBox.isChecked=false
            }else{
                binding.checkBox.isChecked = checkedPosition==absoluteAdapterPosition
            }
            binding.genreName.text=genre.name
            binding.checkBox.setOnClickListener{
                if(checkedPosition!=absoluteAdapterPosition){
                    notifyItemChanged(checkedPosition)
                    checkedPosition=absoluteAdapterPosition
                    listener.onGenreClick(genre)
                }else{
                    checkedPosition = -1
                    listener.onGenreClick(null)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            GenreBottomSheetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return genreList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(genreList[holder.absoluteAdapterPosition])
    }
}