package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants.SHARED.PERSON_EMAIL
import com.example.tasks.service.constants.TaskConstants.SHARED.PERSON_KEY
import com.example.tasks.service.constants.TaskConstants.SHARED.PERSON_NAME
import com.example.tasks.service.constants.TaskConstants.SHARED.TOKEN_KEY
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.ApiHeaderModel
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.local.SecurityPreferences

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application)
    private val sharedPreferences = SecurityPreferences(application)

    private val mutableCreate = MutableLiveData<ValidationListener>()
    var create: LiveData<ValidationListener> = mutableCreate

    fun create(name: String, email: String, password: String) {
        personRepository.create(name, email, password, object : ApiListener<ApiHeaderModel> {
            override fun onSuccess(model: ApiHeaderModel) {
                sharedPreferences.store(TOKEN_KEY, model.token)
                sharedPreferences.store(PERSON_KEY, model.personKey)
                sharedPreferences.store(PERSON_NAME, model.name)
                sharedPreferences.store(PERSON_EMAIL, email)

                mutableCreate.value = ValidationListener()
            }

            override fun onFailure(string: String) {
                mutableCreate.value = ValidationListener(string)
            }

        })

    }

}