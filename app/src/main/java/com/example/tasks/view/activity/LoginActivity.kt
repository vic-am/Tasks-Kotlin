package com.example.tasks.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.service.FingerprintHelper
import com.example.tasks.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        setListeners()
        observe()
        verifyLoggedUser()

        FingerprintHelper.isAuthenticationAvailable(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_login) {
            handleLogin()
        } else if (v.id == R.id.text_register) {
            intentToRegisterActivity()
        }
    }

    private fun setListeners() {
        button_login.setOnClickListener(this)
        text_register.setOnClickListener(this)
    }

    private fun verifyLoggedUser() {
        viewModel.verifyLoggedUser()
    }

    private fun observe() {
        viewModel.login.observe(this, Observer {
            if (it.getStatus()) {
                intentToMainActivity()
                finish()

            } else {
                Toast.makeText(this, it.getMessage(), Toast.LENGTH_SHORT).show()

            }
        })

        viewModel.loggedUser.observe(this, Observer {
            if (it) {
                intentToMainActivity()
                finish()
                
            }
        })
    }

    private fun handleLogin() {
        val email = edit_email.text.toString()
        val password = edit_password.text.toString()

        viewModel.doLogin(email, password)
    }

    private fun intentToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun intentToRegisterActivity() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

}
