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

class UserLoginViewModel: ViewModel() {

    private var disposable = CompositeDisposable()
    private var userRepository=UserRepository()

    var user=MutableLiveData<User>()
    var errorLiveData = MutableLiveData<String>()

    fun findUserByAccountID(id:Int) {
        disposable.add(userRepository.findByAccountID(id)
            .subscribe({ jsonElement ->
                user.postValue(User.getUser(jsonElement))
            }, { error ->
                errorLiveData.postValue("Error loading user: ${error.message}")
            })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}