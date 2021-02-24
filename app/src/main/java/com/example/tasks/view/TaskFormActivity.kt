package com.example.tasks.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.viewmodel.TaskFormViewModel
import kotlinx.android.synthetic.main.activity_register.button_save
import kotlinx.android.synthetic.main.activity_task_form.*
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: TaskFormViewModel
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        viewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)

        listeners()
        observe()

        viewModel.listPriorities()

    }

    override fun onClick(v: View) {
        when (v.id) {
//            R.id.button_save -> {
//                val name = edit_name.text.toString()
//                val email = edit_email.text.toString()
//                val password = edit_password.text.toString()
//
//                viewModel.create(name, email, password)
//            }

            R.id.button_date -> showDatePicker()
        }
    }

    private fun showDatePicker() {
        val userCalendar = Calendar.getInstance()
        val year = userCalendar.get(Calendar.YEAR)
        val month = userCalendar.get(Calendar.MONTH)
        val day = userCalendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, this, year, month, day).show()
    }

    private fun observe() {
        viewModel.priorityList.observe(this, Observer {
            val list: MutableList<String> = arrayListOf()
            for (item in it) {
                list.add(item.description)
            }

            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                list
            )
            spinner_priority.adapter = adapter

        })
    }

    private fun listeners() {
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
