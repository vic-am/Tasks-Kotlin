package com.example.tasks.view.viewholder

import android.app.AlertDialog
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.example.tasks.service.listener.TaskListener
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository
import java.text.SimpleDateFormat
import java.util.*

class TaskViewHolder(itemView: View, val listener: TaskListener) :
    RecyclerView.ViewHolder(itemView) {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    private val priorityRepository = PriorityRepository(itemView.context)

    private var taskContainer: View = itemView.findViewById(R.id.task_container)
    private var textDescription: TextView = itemView.findViewById(R.id.text_description)
    private var textPriority: TextView = itemView.findViewById(R.id.text_priority)
    private var textDueDate: TextView = itemView.findViewById(R.id.text_due_date)
    private var imageTask: ImageView = itemView.findViewById(R.id.image_task)

    /**
     * Atribui valores aos elementos de interface e também eventos
     */

    fun bindData(task: TaskModel) {

        this.textDescription.text = task.description
        this.textPriority.text = priorityRepository.priorityDescription(task.priorityId)

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(task.dueData)
        if (date != null) this.textDueDate.text = dateFormat.format(date)

        if (task.complete) {
            textDescription.setTextColor(Color.GRAY)
            imageTask.setImageResource(R.drawable.ic_done)
        } else {
            textDescription.setTextColor(Color.BLACK)
            imageTask.setImageResource(R.drawable.ic_todo)
        }

        // Eventos
        taskContainer.setOnClickListener { listener.onListClick(task.id) }
        imageTask.setOnClickListener {
            if (task.complete) {
                listener.onUndoClick(task.id)
            } else {
                listener.onCompleteClick(task.id)
            }
        }


        textDescription.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle(R.string.remocao_de_tarefa)
                .setMessage(R.string.remover_tarefa)
                .setPositiveButton(R.string.sim) { dialog, which ->
                    listener.onDeleteClick(task.id)
                }
                .setNeutralButton(R.string.cancelar, null)
                .show()
            true
        }

    }

}