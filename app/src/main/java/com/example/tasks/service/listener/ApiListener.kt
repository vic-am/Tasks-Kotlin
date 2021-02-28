package com.example.tasks.service.listener

import com.example.tasks.service.model.HeaderModel

interface ApiListener<T> {

    fun onSuccess(model: T)

    fun onFailure(string: String)

}