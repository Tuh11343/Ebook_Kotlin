package com.example.connectnodejs.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connectnodejs.model.Genre
import com.example.connectnodejs.model.Author
import com.example.connectnodejs.model.Book
import com.example.connectnodejs.model.Favorite
import com.example.connectnodejs.repository.AuthorRepository
import com.example.connectnodejs.repository.BookRepository
import com.example.connectnodejs.repository.FavoriteRepository
import com.example.connectnodejs.repository.GenreRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

class BookDetailViewModel:ViewModel() {

    private var disposable = CompositeDisposable()
    private var authorRepository = AuthorRepository()
    private var genreRepository=GenreRepository()
    private var bookRepository=BookRepository()
    private var favoriteRepository=FavoriteRepository()


    var errorLiveData=MutableLiveData<String>()
    var authorList=MutableLiveData<MutableList<Author>>()
    var genreList= MutableLiveData<MutableList<Genre>>()
    var authorBookList=MutableLiveData<MutableList<Book>>()
    var favoriteClick=MutableLiveData<Boolean>()

    fun findAuthorsByBookID(id:Int,limit: Int?, offset: Int?){
        disposable.add(authorRepository.findByBookID(id,limit,offset)
            .subscribe({ jsonElement ->
                authorList.postValue(Author.getAuthorList(jsonElement))
            }, { error ->
                errorLiveData.postValue("Error loading genres: ${error.message}")
            })
        )
    }

    fun findGenresByBookID(id:Int,limit: Int?, offset: Int?){
        disposable.add(genreRepository.findByBookID(id,limit,offset)
            .subscribe({ jsonElement ->
                genreList.postValue(Genre.getGenreList(jsonElement))
            }, { error ->
                errorLiveData.postValue("Error loading genres: ${error.message}")
            })
        )
    }

    fun findBooksByAuthorID(id:Int,limit: Int?, offset: Int?){
        disposable.add(bookRepository.findByGenreID(id,limit,offset)
            .subscribe({ jsonElement ->
                authorBookList.postValue(Book.getBookList(jsonElement))
            }, { error ->
                errorLiveData.postValue("Error loading genres: ${error.message}")
            })
        )
    }

    fun favoriteClick(user_id:Int,book_id:Int){
        disposable.add(favoriteRepository.favoriteClick(user_id,book_id)
            .subscribe({ jsonElement ->
                favoriteClick.postValue(Favorite.getAction(jsonElement))
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