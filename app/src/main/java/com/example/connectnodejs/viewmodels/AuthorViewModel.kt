package com.example.connectnodejs.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connectnodejs.model.Author
import com.example.connectnodejs.model.Book
import com.example.connectnodejs.repository.AuthorRepository
import com.example.connectnodejs.repository.BookRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

class AuthorViewModel: ViewModel() {

    private var disposable = CompositeDisposable()
    private var bookRepository= BookRepository()

    var bookList=MutableLiveData<MutableList<Book>>()
    var errorLiveData = MutableLiveData<String>()

    fun findBookList(id:Int) {
        disposable.add(bookRepository.findByAuthorID(id)
            .subscribe({ jsonElement ->
                bookList.postValue(Book.getBookList(jsonElement))
            }, { error ->
                errorLiveData.postValue("Error loading genres: ${error.message}")
            })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}