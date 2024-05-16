package com.example.connectnodejs.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connectnodejs.model.Author
import com.example.connectnodejs.model.Book
import com.example.connectnodejs.model.Genre
import com.example.connectnodejs.repository.AuthorRepository
import com.example.connectnodejs.repository.BookRepository
import com.example.connectnodejs.repository.GenreRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

class HomeViewModel:ViewModel() {

    private var disposable = CompositeDisposable()
    private var authorRepository = AuthorRepository()
    private var bookRepository=BookRepository()
    private var genreRepository=GenreRepository()

    var authorList = MutableLiveData<MutableList<Author>>()
    var genreList=MutableLiveData<MutableList<Genre>>()
    var bigCardBookList=MutableLiveData<MutableList<Book>>()
    var premiumBookList= MutableLiveData<MutableList<Book>>()
    var normalBookList=MutableLiveData<MutableList<Book>>()
    var topRatingBookList= MutableLiveData<MutableList<Book>>()

    var errorLiveData = MutableLiveData<String>()

    fun findAllBookList(limit: Int?, offset: Int?) {
        disposable.add(bookRepository.findAll(12,offset)
            .subscribe({ jsonElement ->
                bigCardBookList.postValue(Book.getBookList(jsonElement))
            }, { error ->
                errorLiveData.postValue("Error loading genres: ${error.message}")
            })
        )
    }

    fun findPremiumBook(limit: Int?, offset: Int?) {
        disposable.add(bookRepository.findPremiumBook(limit, offset)
            .subscribe({ jsonElement ->
                premiumBookList.postValue(Book.getBookList(jsonElement))
            }, { error ->
                errorLiveData.postValue("Error find normal book: ${error.message}")
            })
        )
    }

    fun findNormalBook(limit: Int?, offset: Int?) {
        disposable.add(bookRepository.findNormalBook(limit, offset)
            .subscribe({ jsonElement ->
                normalBookList.postValue(Book.getBookList(jsonElement))
            }, { error ->
                errorLiveData.postValue("Error find premium book: ${error.message}")
            })
        )
    }

    fun findTopRatingBook(limit: Int?, offset: Int?) {
        disposable.add(bookRepository.findByTopRating(limit, offset)
            .subscribe({ jsonElement ->
                topRatingBookList.postValue(Book.getBookList(jsonElement))
            }, { error ->
                errorLiveData.postValue("Error find top rating book: ${error.message}")
            })
        )
    }

    fun findAllAuthorList(limit: Int?, offset: Int?) {
        disposable.add(authorRepository.getAllAuthor(limit,offset)
            .subscribe({ jsonElement ->
                authorList.postValue(Author.getAuthorList(jsonElement))
            }, { error ->
                errorLiveData.postValue("Error loading genres: ${error.message}")
            })
        )
    }

    fun findAllGenre(limit: Int?, offset: Int?) {
        disposable.add(genreRepository.getAllGenres(limit,offset)
            .subscribe({ jsonElement ->
                genreList.postValue(Genre.getGenreList(jsonElement))
            }, { error ->
                errorLiveData.postValue("Error loading genres: ${error.message}")
            })
        )
    }

}