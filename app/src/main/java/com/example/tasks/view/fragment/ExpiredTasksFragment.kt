package com.example.tasks.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.viewmodel.ExpiredTasksViewModel

class ExpiredTasksFragment : Fragment() {

    private lateinit var viewModel: ExpiredTasksViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(ExpiredTasksViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_expired_tasks, container, false)

        return root
    }
}
