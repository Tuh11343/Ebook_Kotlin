package com.example.connectnodejs.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connectnodejs.model.Author
import com.example.connectnodejs.model.Book
import com.example.connectnodejs.model.Lyric
import com.example.connectnodejs.repository.AuthorRepository
import com.example.connectnodejs.repository.BookRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

class AudioViewModel : ViewModel() {

    private var disposable = CompositeDisposable()
    private var authorRepository=AuthorRepository()

    var isReadBook = MutableLiveData<Boolean>()
    var currentLyric = MutableLiveData<String>()
    var totalLyrics = MutableLiveData<String>()
    var lyricList = MutableLiveData<MutableList<Lyric>>()
    var start = MutableLiveData<Int>()
    var bookAuthorName=MutableLiveData<String>()

    fun findBookAuthorName(id:Int) {
        disposable.add(authorRepository.findOneByBookID(id)
            .subscribe({ jsonElement ->
                bookAuthorName.value=Author.getAuthorName(jsonElement)
            }, { error ->

            })
        )
    }

    fun updateTotalLyrics(totalLyrics: String) {
        this.totalLyrics.postValue(totalLyrics)
    }

    fun updateLyric(lyric: String) {
        currentLyric.postValue(lyric)
    }

    fun updateStart(start: Int) {
        this.start.postValue(start)
    }

    fun updateLyricList(lyricList: MutableList<Lyric>) {
        this.lyricList.postValue(lyricList)
    }

    fun updateIsReadBook(isReadBook: Boolean) {
        this.isReadBook.postValue(isReadBook)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

}