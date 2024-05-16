package com.example.connectnodejs.listener

import com.example.connectnodejs.adapter.SortBottomSheetAdapter
import com.example.connectnodejs.model.Genre

interface SortListener {

    fun onSortClick(sortType: SortBottomSheetAdapter.SortType)

}