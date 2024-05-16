package com.example.connectnodejs.listener

import com.example.connectnodejs.model.Book

interface SearchBookListener {

    fun onBookClick(book:Book)

}