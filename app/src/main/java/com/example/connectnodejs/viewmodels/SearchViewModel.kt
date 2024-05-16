package com.example.connectnodejs.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connectnodejs.model.Book
import com.example.connectnodejs.model.Genre
import com.example.connectnodejs.repository.BookRepository
import com.example.connectnodejs.repository.GenreRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

class SearchViewModel : ViewModel() {

    private var disposable = CompositeDisposable()
    private var genreRepository = GenreRepository()
    private var bookRepository = BookRepository()

    var filterGenreList = MutableLiveData<MutableList<Genre>>()
    var bookList = MutableLiveData<MutableList<Book>>()
    var length=MutableLiveData<Int>()
    var loadMoreBookList= MutableLiveData<MutableList<Book>>()
    var errorLiveData = MutableLiveData<String>()

    fun loadFilterGenres(limit: Int?, offset: Int?) {
        disposable.add(
            genreRepository.getAllGenres(limit, offset)
                .subscribe({ jsonElement ->
                    filterGenreList.postValue(Genre.getGenreList(jsonElement))
                }, { error ->
                    errorLiveData.value = "Error loading genres: ${error.message}"
                })
        )
    }

    fun loadBookList(name:String,genreID: Int?, limit: Int?, offset: Int?) {
        if (genreID != null) {
            disposable.add(
                bookRepository.findByNameAndGenre(name,genreID,limit,offset)
                    .subscribe({ jsonElement ->
                        bookList.value=Book.getBookList(jsonElement)
                        length.value=Book.getLength(jsonElement)
                        Log.i("Nothing","Length:${length.value}")
                    }, { error ->
                        errorLiveData.value = "Error loading book: ${error.message}"
                        Log.i("ERROR","Error:${error.message}")
                    })
            )
        } else {
            disposable.add(
                bookRepository.findByName(name,limit, offset)
                    .subscribe({ jsonElement ->
                        bookList.value=Book.getBookList(jsonElement)
                        length.value=Book.getLength(jsonElement)

                        Log.i("Nothing","Length:${length.value}")
                    }, { error ->
                        errorLiveData.value = "Error loading book: ${error.message}"
                        Log.i("ERROR","Error:${error.message}")
                    })
            )
        }
    }

    fun loadMoreBookList(name:String,genreID: Int?, limit: Int?, offset: Int?) {
        if (genreID != null) {
            disposable.add(
                bookRepository.findByNameAndGenre(name,genreID, limit, offset)
                    .subscribe({ jsonElement ->
                        loadMoreBookList.postValue(Book.getBookList(jsonElement))
                    }, { error ->
                        errorLiveData.value = "Error loading book: ${error.message}"
                    })
            )
        } else {
            disposable.add(
                bookRepository.findByName(name, limit, offset)
                    .subscribe({ jsonElement ->
                        loadMoreBookList.postValue(Book.getBookList(jsonElement))
                    }, { error ->
                        errorLiveData.value = "Error loading book: ${error.message}"
                    })
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

}