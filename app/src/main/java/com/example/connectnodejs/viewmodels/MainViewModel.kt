package com.example.connectnodejs.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connectnodejs.model.Account
import com.example.connectnodejs.model.Author
import com.example.connectnodejs.model.Book
import com.example.connectnodejs.model.Subscription
import com.example.connectnodejs.model.SubscriptionHistory
import com.example.connectnodejs.repository.AccountRepository
import com.example.connectnodejs.repository.SubscriptionHistoryRepository
import com.example.connectnodejs.repository.SubscriptionRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.Stack

class MainViewModel : ViewModel() {

    private var accountRepository = AccountRepository()
    private var subscriptionRepository = SubscriptionRepository()
    private var subscriptionHistoryRepository = SubscriptionHistoryRepository()
    private var disposable = CompositeDisposable()


//    var selectedBook = MutableLiveData<Book?>()
//    var selectedAuthor = MutableLiveData<Author?>()
//    var lastState = MutableLiveData<CurrentState>()
//    var lastState2 = MutableLiveData<CurrentState?>()
//    var currentState = MutableLiveData(CurrentState.Home)
    var appBarVisibility = MutableLiveData<Boolean>()
    var songControlVisibility = MutableLiveData<Boolean>()
    var bottomBarVisibility = MutableLiveData<Boolean>()
    var currentAccount = MutableLiveData<Account>()
    var currentAccountSubscription = MutableLiveData<Subscription>()
    var currentAccountSubscriptionHistory = MutableLiveData<SubscriptionHistory>()
    var errorLiveData = MutableLiveData<String>()
    var resetAudio = MutableLiveData<Unit>()
    var bottomBarTab = MutableLiveData<Int>()
    var songControlBook=MutableLiveData<Book>()

    var appState = MutableLiveData<Stack<State>>()
    private var selectedBookStack = MutableLiveData<Stack<Book>>()
    private var selectedAuthorStack = MutableLiveData<Stack<Author>>()

    private var updateSubscription = MutableLiveData<Unit>()
    private var updateSubscriptionHistory = MutableLiveData<Unit>()

    fun updateSubscription(subscription: Subscription) {
        disposable.add(
            subscriptionRepository.update(subscription)
                .subscribe({ jsonElement ->
                    updateSubscription.postValue(Unit)

                }, { error ->
                    errorLiveData.value = "Error update subscription: ${error.message}"
                })
        )
    }

    fun updateSubscriptionHistory(subscriptionHistory: SubscriptionHistory) {
        disposable.add(
            subscriptionHistoryRepository.update(subscriptionHistory)
                .subscribe({ jsonElement ->
                    updateSubscriptionHistory.postValue(Unit)

                }, { error ->
                    errorLiveData.value = "Error update subscription history: ${error.message}"
                })
        )
    }


    fun findAccountSubscription(accountID: Int) {
        disposable.add(
            subscriptionRepository.getSubscriptionByAccountID(accountID)
                .subscribe({ jsonElement ->
                    currentAccountSubscription.postValue(Subscription.getSubscription(jsonElement))

                }, { error ->
                    errorLiveData.value = "Error loading genres: ${error.message}"
                })
        )
    }

    fun findAccountSubscriptionHistory(subscriptionID: Int) {
        disposable.add(
            subscriptionHistoryRepository.findBySubscriptionID(subscriptionID)
                .subscribe({ jsonElement ->
                    currentAccountSubscriptionHistory.postValue(SubscriptionHistory.getSubscriptionHistory(jsonElement))

                }, { error ->
                    errorLiveData.value =
                        "Error find subscription history by subscription id: ${error.message}"
                })
        )
    }

    fun findAccountByID(accountID: Int) {
        disposable.add(
            accountRepository.findByID(accountID)
                .subscribe({ jsonElement ->
                    currentAccount.postValue(Account.getAccount(jsonElement))
                }, { error ->
                    errorLiveData.postValue("Error find account by id: ${error.message}")
                })
        )
    }

//    fun updateSelectedBook(book: Book?) {
//        selectedBook.value = book
//    }
//
//    fun updateSelectedAuthor(author: Author?) {
//        selectedAuthor.postValue(author)
//    }
//
//    fun updateCurrentState(state: CurrentState) {
//        lastState.value = currentState.value
//        currentState.postValue(state)
//    }
//
//    fun updateLastState(state: CurrentState) {
//        lastState.postValue(state)
//    }
//
//    fun updateLastState2(state: CurrentState?) {
//        lastState2.value = state
//    }

    fun updateAppBarVisibility(boolean: Boolean) {
        appBarVisibility.postValue(boolean)
    }

    fun updateSongControlVisibility(boolean: Boolean) {
        songControlVisibility.postValue(boolean)
    }

    fun updateBottomBarVisibility(boolean: Boolean) {
        bottomBarVisibility.postValue(boolean)
    }

    fun updateBottomBarTab(index: Int) {
        bottomBarTab.value = index
    }

    fun resetAudio() {
        resetAudio.postValue(Unit)
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    //Must call first in the app to create stack
    fun createAppState() {
        appState.value = Stack()
        selectedBookStack.value = Stack()
        selectedAuthorStack.value = Stack()
    }

    fun resetAppState(state:State){
        val newStateStack = appState.value ?: Stack()
        newStateStack.removeAllElements()
        newStateStack.push(state)
        appState.value = newStateStack
    }

    fun addState(state: State) {
        val newStateStack = appState.value ?: Stack()
        newStateStack.push(state)
        appState.value = newStateStack
    }

    fun updateState(state:State){
        val newStateStack=appState.value?:Stack()
        newStateStack[newStateStack.size-1] = state
        appState.value=newStateStack
    }

    fun returnLastState(){
        if(appState.value!!.peek()==State.DetailBook){
            removeSelectedBook()
        }else if(appState.value!!.peek()==State.Author){
            removeSelectedAuthor()
        }

        val newStateStack = appState.value ?: Stack()
        newStateStack.pop()
        appState.value = newStateStack
    }

    fun addSelectedBook(book:Book){
        val newStack=selectedBookStack.value?:Stack()
        newStack.push(book)
        selectedBookStack.value=newStack
    }

    fun addSelectedAuthor(author:Author){
        val newStack=selectedAuthorStack.value?:Stack()
        newStack.push(author)
        selectedAuthorStack.value=newStack
    }

    private fun removeSelectedBook(){
        if(selectedBookStack.value!!.size>0){
            selectedBookStack.value!!.pop()
        }
    }

    private fun removeSelectedAuthor(){
        selectedAuthorStack.value!!.pop()
    }

    fun getSelectedBook(): Book? {
        if(selectedBookStack.value!!.size>0){
            return selectedBookStack.value!!.peek()
        }
        return null
    }

    fun getSelectedAuthor(): Author {
        return selectedAuthorStack.value!!.peek()
    }

    companion object {
//        enum class CurrentState {
//            Home, DetailBook, ReadBook, Search, SignIn, User, SignUp, Author, Subscription, Favorite
//        }

        enum class State {
            Home, DetailBook, ReadBook, Search, SignIn, User,UserSignIn, SignUp, Author, Subscription, Favorite
        }
    }
}