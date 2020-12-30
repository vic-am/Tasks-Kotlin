package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.tasks.service.repository.PersonRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository()

    fun doLogin(email: String, password: String) {
        personRepository.login(email, password)
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
    }

}