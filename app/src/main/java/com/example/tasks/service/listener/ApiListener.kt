package com.example.tasks.service.listener

import com.example.tasks.service.HeaderModel

interface ApiListener {

    fun onSuccess(model: HeaderModel)

    fun onFailure(string: String)

}