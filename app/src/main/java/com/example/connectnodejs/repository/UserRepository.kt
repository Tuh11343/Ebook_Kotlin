package com.example.connectnodejs.repository

import android.util.Log
import com.example.connectnodejs.model.User
import com.example.connectnodejs.network.RetrofitClient
import com.example.connectnodejs.network.api.UserAPIService
import com.google.gson.JsonElement
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class UserRepository {

    private val apiService: UserAPIService =
        RetrofitClient.get()!!.create(UserAPIService::class.java)

    fun findByID(id:Int): Single<JsonElement> {
        return apiService.findByID(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Find user by id error: $throwable")
                Single.error(throwable)
            }
    }

    fun findByAccountID(id:Int): Single<JsonElement> {
        return apiService.findByAccountID(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Find user by account id error: $throwable")
                Single.error(throwable)
            }
    }

    fun updateUser(user:User): Single<JsonElement> {
        return apiService.updateUser(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Update user error: $throwable")
                Single.error(throwable)
            }
    }

    fun createUser(user:User): Single<JsonElement> {
        return apiService.createUser(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext { throwable: Throwable ->
                Log.i("ERROR", "Create user error: $throwable")
                Single.error(throwable)
            }
    }

}