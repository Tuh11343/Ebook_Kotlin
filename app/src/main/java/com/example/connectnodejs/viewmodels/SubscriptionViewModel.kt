package com.example.connectnodejs.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connectnodejs.model.Account
import com.example.connectnodejs.model.Subscription
import com.example.connectnodejs.model.SubscriptionHistory
import com.example.connectnodejs.repository.AccountRepository
import com.example.connectnodejs.repository.PaymentRepository
import com.example.connectnodejs.repository.SubscriptionHistoryRepository
import com.example.connectnodejs.repository.SubscriptionRepository
import com.google.gson.JsonElement
import io.reactivex.rxjava3.disposables.CompositeDisposable

class SubscriptionViewModel:ViewModel() {

    private var disposable = CompositeDisposable()
    private var subscriptionRepository = SubscriptionRepository()
    private var subscriptionHistoryRepository=SubscriptionHistoryRepository()
    private var paymentRepository=PaymentRepository()
    private var accountRepository=AccountRepository()

    var accountFound=MutableLiveData<Account>()
    var accountSubscription=MutableLiveData<Subscription>()
    var accountSubscriptionHistory=MutableLiveData<SubscriptionHistory>()
    var paymentResponse=MutableLiveData<JsonElement>()
    var errorLiveData = MutableLiveData<String>()
    var updateAccount=MutableLiveData<Unit>()

    fun findAccountSubscription(accountID:Int) {
        disposable.add(subscriptionRepository.getSubscriptionByAccountID(accountID)
            .subscribe({ jsonElement ->
                accountSubscription.postValue(Subscription.getSubscription(jsonElement))

            }, { error ->
                errorLiveData.value = "Error loading genres: ${error.message}"
            })
        )
    }

    fun findAccountByID(accountID:Int) {
        disposable.add(accountRepository.findByID(accountID)
            .subscribe({ jsonElement ->
                if(jsonElement==null){
                    errorLiveData.postValue("")
                }else{
                    accountFound.postValue(Account.getAccount(jsonElement))
                }

            }, { error ->
                errorLiveData.value = "Error loading genres: ${error.message}"
            })
        )
    }

    fun updateAccount(account:Account) {
        disposable.add(accountRepository.updateAccount(account)
            .subscribe({ jsonElement ->
                updateAccount.postValue(Unit)

            }, { error ->
                errorLiveData.value = "Error loading genres: ${error.message}"
            })
        )
    }

    fun updateSubscriptionHistory(subscriptionHistory: SubscriptionHistory) {
        accountSubscriptionHistory.value=subscriptionHistory
        disposable.add(subscriptionHistoryRepository.update(subscriptionHistory)
            .subscribe({ jsonElement ->
                accountSubscriptionHistory.postValue(SubscriptionHistory.getSubscriptionHistory(jsonElement))

            }, { error ->
                errorLiveData.value = "Error update subscription history: ${error.message}"
            })
        )
    }

    fun updateSubscription(subscription: Subscription) {
        accountSubscription.value=subscription
        disposable.add(subscriptionRepository.update(subscription)
            .subscribe({ jsonElement ->
                accountSubscription.postValue(Subscription.getSubscription(jsonElement))

            }, { error ->
                errorLiveData.value = "Error update subscription : ${error.message}"
            })
        )
    }


    fun paymentRequest(accountID:Int,total:Float) {
        disposable.add(paymentRepository.paymentRequest(accountID, total)
            .subscribe({ jsonElement ->
                paymentResponse.postValue(jsonElement)

            }, { error ->
                errorLiveData.value = "Error payment: ${error.message}"
            })
        )
    }




    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

}