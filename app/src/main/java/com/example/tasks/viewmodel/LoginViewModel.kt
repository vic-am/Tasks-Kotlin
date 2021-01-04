package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.HeaderModel
import com.example.tasks.service.constants.TaskConstants.SHARED.PERSON_KEY
import com.example.tasks.service.constants.TaskConstants.SHARED.PERSON_NAME
import com.example.tasks.service.constants.TaskConstants.SHARED.TOKEN_KEY
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.local.SecurityPreferences

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository()
    private val sharedPreferences = SecurityPreferences(application)
    private val mutableLogin = MutableLiveData<Boolean>()
    var login: LiveData<Boolean> = mutableLogin

    fun doLogin(email: String, password: String) {
        personRepository.login(email, password, object : ApiListener {
            override fun onSuccess(model: HeaderModel) {

                sharedPreferences.store(TOKEN_KEY, model.token)
                sharedPreferences.store(PERSON_KEY, model.personKey)
                sharedPreferences.store(PERSON_NAME, model.name)

                mutableLogin.value = true
            }

            override fun onFailure(string: String) {
                mutableLogin.value = false
            }

        })
    }

    fun verifyLoggedUser() {

    }

}