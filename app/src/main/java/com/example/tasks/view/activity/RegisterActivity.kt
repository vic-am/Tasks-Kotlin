package com.example.tasks.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        listeners()
        observe()
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.button_save) {

            val name = edit_name.text.toString()
            val email = edit_email.text.toString()
            val password = edit_password.text.toString()

            viewModel.create(name, email, password)
        }
    }

    private fun observe() {
        viewModel.create.observe(this, Observer {
            if (it.getStatus()) {
                intentToLoginActivity()
            } else {
                Toast.makeText(this, it.getMessage(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun listeners() {
        button_save.setOnClickListener(this)
    }

    private fun intentToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
