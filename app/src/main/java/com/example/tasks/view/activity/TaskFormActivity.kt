package com.example.tasks.view.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.service.model.TaskModel
import com.example.tasks.viewmodel.TaskFormViewModel
import kotlinx.android.synthetic.main.activity_register.button_save
import kotlinx.android.synthetic.main.activity_task_form.*
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: TaskFormViewModel
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    private val priorityIdList: MutableList<Int> = arrayListOf()
    private val priorityDescriptionList: MutableList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        viewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)

        buttonListeners()
        observePriorities()

        viewModel.listPriorities()

    }

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.button_save) {
            handleSave()
        } else if (id == R.id.button_date) {
            showDatePicker()
        }
    }

    private fun handleSave() {
        val task = TaskModel().apply {
            this.description = edit_description.text.toString()
            this.priorityId = priorityIdList[spinner_priority.selectedItemPosition]
            this.dueData = button_date.text.toString()
            this.complete = check_complete.isChecked
        }

        viewModel.save(task)
    }

    private fun showDatePicker() {
        val userCalendar = Calendar.getInstance()
        val year = userCalendar.get(Calendar.YEAR)
        val month = userCalendar.get(Calendar.MONTH)
        val day = userCalendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, this, year, month, day).show()
    }

    private fun observePriorities() {
        viewModel.priorityList.observe(this, Observer {
            for (priority in it) {
                priorityDescriptionList.add(priority.description)
                priorityIdList.add(priority.id)
            }

            val spinnerAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                priorityDescriptionList
            )
            spinner_priority.adapter = spinnerAdapter

        })
    }

    private fun buttonListeners() {
        button_save.setOnClickListener(this)
        button_date.setOnClickListener(this)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val newCalendar = Calendar.getInstance()
        newCalendar.set(year, month, dayOfMonth)

        val formatedCalendar = dateFormat.format(newCalendar.time)
        button_date.text = formatedCalendar

    }

}
