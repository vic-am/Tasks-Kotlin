package com.example.tasks.view.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.service.Utls
import com.example.tasks.service.constants.TaskConstants
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
    private var newTaskId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        viewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)

        buttonListeners()
        observe()

        viewModel.listPriorities()

        loadDataFromActivity()

    }

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.button_save) {
            handleSave()
        } else if (id == R.id.button_date) {
            showDatePicker()
            Utls.closeKeyboard(applicationContext, this)

        }
    }

    private fun loadDataFromActivity() {
        val bundle = intent.extras
        if (bundle != null) {
            newTaskId = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            viewModel.load(newTaskId)
            button_save.text = getString(R.string.update_task)
        } else {

        }
    }

    private fun handleSave() {
        val task = TaskModel().apply {
            this.id = newTaskId
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

    private fun observe() {
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

        viewModel.validation.observe(this, Observer {
            if (it.getStatus()) {
                if (newTaskId == 0) {
                    makeToast(getString(R.string.task_created), Toast.LENGTH_SHORT)
                } else {
                    makeToast(getString(R.string.task_updated), Toast.LENGTH_SHORT)
                }
                finish()
            } else {
                makeToast(it.getMessage(), Toast.LENGTH_SHORT)

            }
        })

        viewModel.task.observe(this, Observer {
            edit_description.setText(it.description)
            check_complete.isChecked = it.complete

            val date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(it.dueData)
            button_date.text = dateFormat.format(date)

            spinner_priority.setSelection(getIndex(it.priorityId))
        })
    }

    private fun makeToast(string: String, lenght: Int) {
        Toast.makeText(this, string, lenght).show()
    }

    private fun getIndex(priorityId: Int): Int {
        var index = 0
        for (i in 0 until priorityIdList.count()) {
            if (priorityIdList[i] == priorityId) {
                index = i
                break
            }
        }
        return index
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
