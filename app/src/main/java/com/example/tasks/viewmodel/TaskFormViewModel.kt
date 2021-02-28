package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val priorityRepository = PriorityRepository(application)
    private val taskRepository = TaskRepository(application)

    private val mutablePriorityList = MutableLiveData<List<PriorityModel>>()
    var priorityList: LiveData<List<PriorityModel>> = mutablePriorityList

    private val mutableValidation = MutableLiveData<ValidationListener>()
    var validation: LiveData<ValidationListener> = mutableValidation

    fun listPriorities() {
        mutablePriorityList.value = priorityRepository.list()
    }

    fun save(task: TaskModel) {
        taskRepository.create(task, object : ApiListener<Boolean>{
            override fun onSuccess(model: Boolean) {
                mutableValidation.value = ValidationListener()
            }

            override fun onFailure(string: String) {
                mutableValidation.value = ValidationListener(string)
            }
        })
    }

}