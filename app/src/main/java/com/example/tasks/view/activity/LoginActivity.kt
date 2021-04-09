package com.example.tasks.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        setListeners()
        observe()

        loadEmail()
        viewModel.isAuthenticationAvailable()
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

    private fun observe() {
        viewModel.login.observe(this, Observer {
            if (it.getStatus()) {
                intentToMainActivity()
                finish()
            } else {
                Toast.makeText(this, it.getMessage(), Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.fingerprint.observe(this, Observer {
            if (it) {
                showAuthentication()
            }
        })
    }

    private fun loadEmail() {
        edit_email.setText(viewModel.loadSavedEmail())
    }

    private fun handleLogin() {
        val email = edit_email.text.toString()
        val password = edit_password.text.toString()

        viewModel.doLogin(email, password)
    }

    private fun showAuthentication() {

        val executor: Executor = ContextCompat.getMainExecutor(this)

        val biometricPrompt = BiometricPrompt(
            this@LoginActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }

            })

        val info: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Olá, ${viewModel.userName}")
            .setSubtitle("Utilize sua digital para ter acesso ao app")
            .setDescription("Caso não seja o ${viewModel.userName} ou queira acessar com sua senha, pressione o botão cancelar")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(info)
    }

    private fun intentToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun intentToRegisterActivity() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

}
