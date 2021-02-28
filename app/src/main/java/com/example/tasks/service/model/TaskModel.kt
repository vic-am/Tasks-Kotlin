package com.example.tasks.service.model

import com.google.gson.annotations.SerializedName

data class TaskModel(
    @SerializedName("Id")
    val id: Int = 0,
    @SerializedName("Description")
    var description: String = "",
    @SerializedName("PriorityId")
    var priorityId: Int = 0,
    @SerializedName("DueDate")
    var dueData: String = "",
    @SerializedName("Complete")
    var complete: Boolean = false
) {
}