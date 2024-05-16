package com.example.connectnodejs.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connectnodejs.model.Account
import com.example.connectnodejs.model.Subscription
import com.example.connectnodejs.model.SubscriptionHistory
import com.example.connectnodejs.model.User
import com.example.connectnodejs.repository.AccountRepository
import com.example.connectnodejs.repository.SubscriptionHistoryRepository
import com.example.connectnodejs.repository.SubscriptionRepository
import com.example.connectnodejs.repository.UserRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

class SignUpViewModel: ViewModel() {

    private var disposable = CompositeDisposable()
    private var accountRepository=AccountRepository()
    private var userRepository=UserRepository()
    private var subscriptionRepository=SubscriptionRepository()
    private var subscriptionHistoryRepository=SubscriptionHistoryRepository()

    var createdAccount=MutableLiveData<Account>()
    var createdUser=MutableLiveData<User>()
    var createdSubscriptionHistory=MutableLiveData<SubscriptionHistory>()
    var createdSubscription=MutableLiveData<Subscription>()
    var createdGoogleSubscription=MutableLiveData<Subscription>()
    var googleAccount=MutableLiveData<Boolean>()

    var updateAccountUnit=MutableLiveData<Unit>()
    var findAccount=MutableLiveData<Account>()
    var errorLiveData = MutableLiveData<String>()

    fun createAccount(account: Account) {
        disposable.add(accountRepository.createAccount(account)
            .subscribe({ jsonElement ->
                createdAccount.postValue(Account.getAccount(jsonElement))
            }, { error ->
                errorLiveData.postValue("Error create account: ${error.message}")
            })
        )
    }

    fun createUser(user:User) {
        disposable.add(userRepository.createUser(user)
            .subscribe({ jsonElement ->
                createdUser.postValue(User.getUser(jsonElement))
            }, { error ->
                errorLiveData.postValue("Error create user: ${error.message}")
            })
        )
    }

    fun createSubscription(subscription: Subscription) {
        disposable.add(subscriptionRepository.create(subscription)
            .subscribe({ jsonElement ->
                createdSubscription.postValue(Subscription.getSubscription(jsonElement))
            }, { error ->
                errorLiveData.postValue("Error create subscription: ${error.message}")
            })
        )
    }

    fun createGoogleSubscription(subscription: Subscription) {
        disposable.add(subscriptionRepository.create(subscription)
            .subscribe({ jsonElement ->
                createdGoogleSubscription.postValue(Subscription.getSubscription(jsonElement))
            }, { error ->
                errorLiveData.postValue("Error create subscription: ${error.message}")
            })
        )
    }

    fun createSubscriptionHistory(subscriptionHistory: SubscriptionHistory) {
        disposable.add(subscriptionHistoryRepository.create(subscriptionHistory)
            .subscribe({ jsonElement ->
                createdSubscriptionHistory.postValue(SubscriptionHistory.getSubscriptionHistory(jsonElement))
            }, { error ->
                errorLiveData.postValue("Error create subscription history: ${error.message}")
            })
        )
    }


    fun findAccountByEmail(email:String) {
        disposable.add(accountRepository.findByEmail(email)
            .subscribe({ jsonElement ->
                findAccount.postValue(Account.getAccount(jsonElement))
            }, { error ->
                errorLiveData.postValue("Error find account by email: ${error.message}")
            })
        )
    }

    fun updateAccount(account: Account) {
        disposable.add(accountRepository.updateAccount(account)
            .subscribe({ jsonElement ->
                updateAccountUnit.postValue(Unit)
            }, { error ->
                errorLiveData.postValue("Error update account: ${error.message}")
            })
        )
    }

    fun updateGoogleAccount(boolean: Boolean){
        googleAccount.postValue(boolean)
    }



    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}