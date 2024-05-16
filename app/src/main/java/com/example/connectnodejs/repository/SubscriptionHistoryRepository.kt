package com.example.connectnodejs.repository

import android.util.Log
import com.example.connectnodejs.model.SubscriptionHistory
import com.example.connectnodejs.network.RetrofitClient
import com.example.connectnodejs.network.api.SubscriptionAPIService
import com.example.connectnodejs.network.api.SubscriptionHistoryAPIService
import com.google.gson.JsonElement
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class SubscriptionHistoryRepository {

    private val apiService: SubscriptionHistoryAPIService =
        RetrofitClient.get()!!.create(SubscriptionHistoryAPIService::class.java)

    fun create(subscriptionHistory:SubscriptionHistory): Single<JsonElement> {
        return apiService.createSubscriptionHistory(subscriptionHistory)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Create subscriptionHistory error: $throwable")
                Single.error(throwable)
            }
    }

    fun update(subscriptionHistory:SubscriptionHistory): Single<JsonElement> {
        return apiService.updateSubscriptionHistory(subscriptionHistory)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Create subscriptionHistory error: $throwable")
                Single.error(throwable)
            }
    }

    fun findByID(id:Int): Single<JsonElement> {
        return apiService.findByID(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Find subscriptionHistory by id error: $throwable")
                Single.error(throwable)
            }
    }

    fun findBySubscriptionID(id:Int): Single<JsonElement> {
        return apiService.findBySubscriptionID(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Find subscriptionHistory by subscription id error: $throwable")
                Single.error(throwable)
            }
    }


}