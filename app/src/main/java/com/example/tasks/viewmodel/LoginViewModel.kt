package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.FingerprintHelper
import com.example.tasks.service.constants.TaskConstants.SHARED.PERSON_EMAIL
import com.example.tasks.service.constants.TaskConstants.SHARED.PERSON_KEY
import com.example.tasks.service.constants.TaskConstants.SHARED.PERSON_NAME
import com.example.tasks.service.constants.TaskConstants.SHARED.TOKEN_KEY
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.ApiHeaderModel
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application)
    private val priorityRepository = PriorityRepository(application)
    private val sharedPreferences = SecurityPreferences(application)

    private val mutableLogin = MutableLiveData<ValidationListener>()
    var login: LiveData<ValidationListener> = mutableLogin

    private val mutableFingerprint = MutableLiveData<Boolean>()
    var fingerprint: LiveData<Boolean> = mutableFingerprint

    var userName = ""

    fun doLogin(email: String, password: String) {
        personRepository.login(email, password, object : ApiListener<ApiHeaderModel> {
            override fun onSuccess(model: ApiHeaderModel) {
                sharedPreferences.store(TOKEN_KEY, model.token)
                sharedPreferences.store(PERSON_KEY, model.personKey)
                sharedPreferences.store(PERSON_NAME, model.name)
                sharedPreferences.store(PERSON_EMAIL, email)

                RetrofitClient.addHeader(model.token, model.personKey)

                mutableLogin.value = ValidationListener()
            }

            override fun onFailure(string: String) {
                mutableLogin.value = ValidationListener(string)
            }
        })
    }

    fun isAuthenticationAvailable() {

        val token = sharedPreferences.get(TOKEN_KEY)
        val person = sharedPreferences.get(PERSON_KEY)
        userName = sharedPreferences.get(PERSON_NAME)

        val isLogged = (token != "" && person != "")

        RetrofitClient.addHeader(token, person)

        if (!isLogged) priorityRepository.allPriorities()

        if (FingerprintHelper.isAuthenticationAvailable(getApplication())) {
            mutableFingerprint.value = isLogged
        }
    }

    fun loadSavedEmail(): String {
        return if (sharedPreferences.get(PERSON_EMAIL) != "") {
            sharedPreferences.get(PERSON_EMAIL)
        } else ""

    }
}