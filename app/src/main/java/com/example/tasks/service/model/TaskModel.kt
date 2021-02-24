package com.example.tasks.service.model

data class TaskModel(
    val id: Int = 0,
    var description: String = "",
    var priorityId: Int = 0,
    var dueData: String = "",
    var complete: Boolean = false
) {
}