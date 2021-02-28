package com.example.tasks.service.listener

class ValidationListener(receivedMessage: String = "") {

    private var status: Boolean = true
    private var message: String = ""

    init {
        if (receivedMessage != "") {
            status = false
            message = receivedMessage
        }
    }

    fun getStatus() = status
    fun getMessage() = message

}