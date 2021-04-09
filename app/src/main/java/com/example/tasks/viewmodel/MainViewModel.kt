package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.repository.local.SecurityPreferences

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = SecurityPreferences(application)

    private val mutableUserName = MutableLiveData<String>()
    var userName: LiveData<String> = mutableUserName

    private val mutableLogout = MutableLiveData<Boolean>()
    var logout: LiveData<Boolean> = mutableLogout

    fun loadUserName() {
        mutableUserName.value = sharedPreferences.get(TaskConstants.SHARED.PERSON_NAME)
    }

    fun logout() {
        sharedPreferences.remove(TaskConstants.SHARED.TOKEN_KEY)
        sharedPreferences.remove(TaskConstants.SHARED.PERSON_KEY)
        sharedPreferences.remove(TaskConstants.SHARED.PERSON_NAME)
        sharedPreferences.remove(TaskConstants.SHARED.PERSON_EMAIL)

        mutableLogout.value = true
    }

}