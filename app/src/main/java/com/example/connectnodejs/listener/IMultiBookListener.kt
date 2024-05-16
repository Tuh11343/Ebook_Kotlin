package com.example.connectnodejs.listener

import com.example.connectnodejs.model.Book

interface IMultiBookListener {

    fun onFirstBookClick(book:Book)

    fun onSecondBookClick(book:Book)

}