package com.example.connectnodejs.repository

import android.util.Log
import com.example.connectnodejs.model.Account
import com.example.connectnodejs.network.RetrofitClient
import com.example.connectnodejs.network.api.AccountAPIService
import com.google.gson.JsonElement
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class AccountRepository {

    private val apiService: AccountAPIService =
        RetrofitClient.get()!!.create(AccountAPIService::class.java)

    fun findByID(id:Int): Single<JsonElement> {
        return apiService.findByID(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Find account by id error: $throwable")
                Single.error(throwable)
            }
    }

    fun signIn(email:String,password:String): Single<JsonElement> {
        return apiService.signIn(email,password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Sign In error: $throwable")
                Single.error(throwable)
            }
    }

    fun findByEmail(email:String): Single<JsonElement> {
        return apiService.findByEmail(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Find account by email error: $throwable")
                Single.error(throwable)
            }
    }

    fun updateAccount(account:Account): Single<JsonElement> {
        return apiService.updateAccount(account)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Update account error: $throwable")
                Single.error(throwable)
            }
    }

    fun createAccount(account:Account): Single<JsonElement> {
        return apiService.createAccount(account)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Create account error: $throwable")
                Single.error(throwable)
            }
    }

}