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

    private val mutableTask = MutableLiveData<TaskModel>()
    var task: LiveData<TaskModel> = mutableTask

    fun listPriorities() {
        mutablePriorityList.value = priorityRepository.list()
    }

    fun save(task: TaskModel) {

        if (task.id == 0) {
            taskRepository.create(task, object : ApiListener<Boolean> {
                override fun onSuccess(model: Boolean) {
                    mutableValidation.value = ValidationListener()
                }

                override fun onFailure(string: String) {
                    mutableValidation.value = ValidationListener(string)
                }
            })
        } else {
            taskRepository.update(task, object : ApiListener<Boolean> {
                override fun onSuccess(model: Boolean) {
                    mutableValidation.value = ValidationListener()
                }

                override fun onFailure(string: String) {
                    mutableValidation.value = ValidationListener(string)
                }
            })
        }
    }

    fun load(id: Int) {
        taskRepository.load(id, object : ApiListener<TaskModel> {
            override fun onSuccess(model: TaskModel) {
                mutableTask.value = model
            }

            override fun onFailure(string: String) {

            }

        })
    }

}