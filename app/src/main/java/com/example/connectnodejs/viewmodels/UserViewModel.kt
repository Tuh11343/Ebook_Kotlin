package com.example.connectnodejs.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connectnodejs.model.Author
import com.example.connectnodejs.model.Book
import com.example.connectnodejs.model.User
import com.example.connectnodejs.repository.AuthorRepository
import com.example.connectnodejs.repository.BookRepository
import com.example.connectnodejs.repository.UserRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

class UserViewModel: ViewModel() {

    private var disposable = CompositeDisposable()
    private var userRepository=UserRepository()

    var errorLiveData = MutableLiveData<String>()


    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}