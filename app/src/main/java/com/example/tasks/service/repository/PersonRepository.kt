package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants.HTTP.SUCCESS
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.model.ApiHeaderModel
import com.example.tasks.service.repository.remote.PersonService
import com.example.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRepository(val context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.createService(PersonService::class.java)

    fun login(email: String, password: String, listener: ApiListener<ApiHeaderModel>) {

        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<ApiHeaderModel> = remote.login(email, password)

        call.enqueue(object : Callback<ApiHeaderModel> {
            override fun onResponse(call: Call<ApiHeaderModel>, response: Response<ApiHeaderModel>) {
                if (response.code() != SUCCESS) {
                    val validation =
                        Gson().fromJson(response.errorBody()?.string(), String::class.java)
                    listener.onFailure(validation)
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<ApiHeaderModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }

    fun create(name: String, email: String, password: String, listener: ApiListener<ApiHeaderModel>) {

        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<ApiHeaderModel> = remote.create(name, email, password, false)

        call.enqueue(object : Callback<ApiHeaderModel> {
            override fun onResponse(call: Call<ApiHeaderModel>, response: Response<ApiHeaderModel>) {
                if (response.code() != SUCCESS) {
                    val validation =
                        Gson().fromJson(response.errorBody()?.string(), String::class.java)
                    listener.onFailure(validation)
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<ApiHeaderModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }
}