package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.remote.RetrofitClient
import com.example.tasks.service.repository.remote.TaskService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(val context: Context) {

    private val remote = RetrofitClient.createService(TaskService::class.java)

    fun all(listener: ApiListener<List<TaskModel>>) {
        val call: Call<List<TaskModel>> = remote.all()
        list(call, listener)
    }

    fun nextWeek(listener: ApiListener<List<TaskModel>>) {
        val call: Call<List<TaskModel>> = remote.nextWeek()
        list(call, listener)
    }

    fun overdue(listener: ApiListener<List<TaskModel>>) {
        val call: Call<List<TaskModel>> = remote.overdue()
        list(call, listener)
    }


    private fun list(call: Call<List<TaskModel>>, listener: ApiListener<List<TaskModel>>) {
        call.enqueue(object : Callback<List<TaskModel>> {
            override fun onResponse(
                call: Call<List<TaskModel>>,
                response: Response<List<TaskModel>>
            ) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation =
                        Gson().fromJson(response.errorBody()?.string(), String::class.java)
                    listener.onFailure(validation)
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<List<TaskModel>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }


    fun create(task: TaskModel, listener: ApiListener<Boolean>) {
        val call: Call<Boolean> =
            remote.create(task.priorityId, task.description, task.dueData, task.complete)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(
                call: Call<Boolean>,
                response: Response<Boolean>
            ) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validation =
                        Gson().fromJson(response.errorBody()?.string(), String::class.java)
                    listener.onFailure(validation)
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }
}