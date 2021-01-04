package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.tasks.service.HeaderModel
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.repository.PersonRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository()

    fun doLogin(email: String, password: String) {
        personRepository.login(email, password, object : ApiListener{
            override fun onSuccess(model: HeaderModel) {

            }

            override fun onFailure(string: String) {

            }

        })
    }

    fun verifyLoggedUser() {

    }

}