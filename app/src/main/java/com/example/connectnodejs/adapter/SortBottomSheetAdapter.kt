package com.example.connectnodejs.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.connectnodejs.databinding.SortBottomSheetItemBinding
import com.example.connectnodejs.listener.SortListener

class SortBottomSheetAdapter(var sortTypeList:MutableList<SortType>,var listener:SortListener):RecyclerView.Adapter<SortBottomSheetAdapter.MyViewHolder>() {

    private var checkedPosition = -1

    inner class MyViewHolder(val binding: SortBottomSheetItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sort:SortType) {
            if(checkedPosition==-1){
                binding.checkBox.isChecked=false
            }else{
                binding.checkBox.isChecked=checkedPosition==absoluteAdapterPosition
            }

            val sortName = when (sort) {
                SortType.YearPublished -> "Năm xuất bản"
                SortType.Name -> "Tên sách"
                SortType.Rating -> "Đánh giá"
                SortType.ID -> "Mặc định"
            }
            binding.sortName.text=sortName
            binding.checkBox.setOnClickListener{
                if(checkedPosition != absoluteAdapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = absoluteAdapterPosition
                    listener.onSortClick(sort)
                } else {
                    checkedPosition = -1
                    listener.onSortClick(SortType.ID)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            SortBottomSheetItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return sortTypeList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(sortTypeList[holder.absoluteAdapterPosition])
    }

    enum class SortType{
        Name,YearPublished,Rating,ID
    }
}