package com.example.connectnodejs.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connectnodejs.model.Genre
import com.example.connectnodejs.repository.GenreRepository

class GenreViewModel : ViewModel() {

    var firstLoadGenreList = MutableLiveData<MutableList<Genre>>()
    var loadMoreGenreList = MutableLiveData<MutableList<Genre>>()
    var length = MutableLiveData<Int>()

    var errorLiveData = MutableLiveData<String>()
    var genreRepository = GenreRepository()

    fun firstLoadGenreList(limit: Int?, offset: Int?) {
        genreRepository.getAllGenres(limit, offset)
            .subscribe({ jsonElement ->
                firstLoadGenreList.value = Genre.getGenreList(jsonElement)
                length.value = Genre.getLength(jsonElement)
            }, { error ->
                errorLiveData.value = "Error loading genres: ${error.message}"
            })
    }

    fun loadMoreGenreList(limit: Int?, offset: Int?) {
        genreRepository.getAllGenres(limit, offset)
            .subscribe({ jsonElement ->
                loadMoreGenreList.value = Genre.getGenreList(jsonElement)
            }, { error ->
                errorLiveData.value = "Error loading genres: ${error.message}"
            })
    }

}