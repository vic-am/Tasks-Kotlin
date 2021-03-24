package com.example.tasks.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.constants.TaskConstants.BUNDLE.TASKFILTER
import com.example.tasks.service.listener.TaskListener
import com.example.tasks.view.activity.TaskFormActivity
import com.example.tasks.view.adapter.TaskAdapter
import com.example.tasks.viewmodel.AllTasksViewModel

class AllTasksFragment : Fragment() {

    private lateinit var viewModel: AllTasksViewModel
    private lateinit var taskListener: TaskListener
    private val adapter = TaskAdapter()
    private var taskFilter = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        viewModel = ViewModelProvider(this).get(AllTasksViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_all_tasks, container, false)

        taskFilter = requireArguments().getInt(TASKFILTER, 0)

        val recycler = root.findViewById<RecyclerView>(R.id.recycler_all_tasks)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        taskListener = object : TaskListener {
            override fun onListClick(id: Int) {
                val intent = Intent(context, TaskFormActivity::class.java)
                val bundle = Bundle()
                bundle.putInt(TaskConstants.BUNDLE.TASKID, id)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            override fun onDeleteClick(id: Int) {
                viewModel.delete(id)
            }

            override fun onCompleteClick(id: Int) {
                viewModel.complete(id)
            }

            override fun onUndoClick(id: Int) {
                viewModel.undo(id)
            }
        }

        observe()

        return root
    }

    override fun onResume() {
        super.onResume()
        adapter.attachListener(taskListener)
        viewModel.list(taskFilter)
    }

    private fun observe() {
        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            if (it.count() >= 0) {
                adapter.updateList(it)
            }
        })

        viewModel.validation.observe(viewLifecycleOwner, Observer {
            if (it.getStatus()) {
                makeToast(getString(R.string.task_removed), Toast.LENGTH_SHORT)
            } else {
                makeToast(it.getMessage(), Toast.LENGTH_SHORT)
            }
        })
    }

    private fun makeToast(string: String, lenght: Int) {
        Toast.makeText(context, string, lenght).show()
    }

}
