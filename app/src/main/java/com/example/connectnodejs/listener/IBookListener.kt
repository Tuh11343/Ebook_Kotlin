package com.example.connectnodejs.listener

import com.example.connectnodejs.model.Book

interface IBookListener {

    fun onBookClick(book:Book)

}