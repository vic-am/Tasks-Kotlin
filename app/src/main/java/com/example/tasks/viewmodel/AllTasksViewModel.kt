package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application)

    private val mutableList = MutableLiveData<List<TaskModel>>()
    var tasks: LiveData<List<TaskModel>> = mutableList

    fun list() {
        taskRepository.all(object : ApiListener<List<TaskModel>> {
            override fun onSuccess(model: List<TaskModel>) {
                mutableList.value = model
            }

            override fun onFailure(string: String) {
                mutableList.value = arrayListOf()
            }
        })
    }

}