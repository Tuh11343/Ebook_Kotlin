package com.example.connectnodejs.listener

import com.example.connectnodejs.model.Genre

interface FilterListener {

    fun onGenreClick(genre:Genre?)

}