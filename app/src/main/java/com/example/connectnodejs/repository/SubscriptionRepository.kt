package com.example.connectnodejs.repository

import android.util.Log
import com.example.connectnodejs.model.Subscription
import com.example.connectnodejs.network.RetrofitClient
import com.example.connectnodejs.network.api.SubscriptionAPIService
import com.google.gson.JsonElement
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class SubscriptionRepository {

    private val apiService: SubscriptionAPIService =
        RetrofitClient.get()!!.create(SubscriptionAPIService::class.java)

    fun getSubscriptionByAccountID(accountID:Int): Single<JsonElement> {
        return apiService.findByAccountID(accountID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Get subscription by accountID error: $throwable")
                Single.error(throwable)
            }
    }

    fun create(subscription: Subscription): Single<JsonElement> {
        return apiService.create(subscription)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Create subscription error: $throwable")
                Single.error(throwable)
            }
    }

    fun update(subscription: Subscription): Single<JsonElement> {
        return apiService.update(subscription)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Create subscription error: $throwable")
                Single.error(throwable)
            }
    }






}