package com.example.tasks.service.model

data class TaskModel(
    val id: Int,
    val description: String,
    val priorityId: Int,
    val dueData: String,
    val complete: Boolean
) {
}