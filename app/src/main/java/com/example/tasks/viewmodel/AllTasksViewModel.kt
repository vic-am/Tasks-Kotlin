package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application)

    private val mutableValidation = MutableLiveData<ValidationListener>()
    var validation: LiveData<ValidationListener> = mutableValidation

    private var taskFilter = 0

    private val mutableList = MutableLiveData<List<TaskModel>>()
    var tasks: LiveData<List<TaskModel>> = mutableList

    fun list(taskFilter: Int) {
        this.taskFilter = taskFilter

        val listener = object : ApiListener<List<TaskModel>> {
            override fun onSuccess(model: List<TaskModel>) {
                mutableList.value = model

            }

            override fun onFailure(string: String) {
                mutableList.value = arrayListOf()
                mutableValidation.value = ValidationListener(string)
            }
        }

        when (this.taskFilter) {
            TaskConstants.FILTER.ALL -> taskRepository.all(listener)
            TaskConstants.FILTER.NEXT -> taskRepository.nextWeek(listener)
            TaskConstants.FILTER.EXPIRED -> taskRepository.overdue(listener)

        }
    }

    fun complete(id: Int) {
        taskRepository.updateStatus(id, true, object : ApiListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                list(taskFilter)
            }

            override fun onFailure(string: String) {}
        })
    }

    fun undo(id: Int) {
        taskRepository.updateStatus(id, false, object : ApiListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                list(taskFilter)
            }

            override fun onFailure(string: String) {}
        })
    }

    fun delete(id: Int) {

        taskRepository.delete(id, object : ApiListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                list(taskFilter)
                mutableValidation.value = ValidationListener()
            }

            override fun onFailure(string: String) {
                mutableValidation.value = ValidationListener(string)
            }
        })
    }

}