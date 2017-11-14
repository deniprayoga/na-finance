package com.example.denip.nasyiatulaisyiyahfinance.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.denip.nasyiatulaisyiyahfinance.main.MainActivity
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val HUNTR = "huntr_LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        checkCurrentUser()
        initLayout()

    }

    private fun initLayout() {
        forgot_password_text_view.setOnClickListener(this)
        sign_in_button.setOnClickListener(this)
    }

    private fun tryLogin() {
        Log.d(HUNTR, "In the tryLogin()")
        val email = email_field.text.toString()
        val password = password_field.text.toString()

        when {
            email.isEmpty() -> showEmptyEmail()
            password.isEmpty() -> showEmptyPassword()
            else -> {
                login(email, password)
                showLoginProgress()
            }
        }
    }

    private fun login(email: String, password: String) {
        Log.d(HUNTR, "In the login()")
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            hideLoginProgress()
            if (!task.isSuccessful) {
                if (password.length < 6) {
                    passwordError()
                } else if (!email.contains(getString(R.string.contain_at_gmail))) {
                    emailError()
                } else {
                    showAuthFailed()
                }
            } else {
                launchMainActivity()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.forgot_password_text_view -> launchForgotPasswordActivity()
            R.id.sign_in_button -> tryLogin()
        }
    }

    private fun launchForgotPasswordActivity() {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }

    private fun checkCurrentUser() {
        if (auth.currentUser != null) {
            launchMainActivity()
        }
    }

    private fun showLoginProgress() {
        login_progress.visibility = View.VISIBLE
        sign_in_button.isEnabled = false
        forgot_password_text_view.isEnabled = false
    }

    private fun hideLoginProgress() {
        login_progress.visibility = View.GONE
        sign_in_button.isEnabled = true
        forgot_password_text_view.isEnabled = true
    }

    private fun launchMainActivity() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    private fun showEmptyEmail() {
        val shake = AnimationUtils.loadAnimation(this@LoginActivity, R.anim.shake)
        email_field?.startAnimation(shake)
    }

    private fun showEmptyPassword() {
        val shake = AnimationUtils.loadAnimation(this@LoginActivity, R.anim.shake)
        password_field?.startAnimation(shake)
    }

    private fun emailError() {
        val shake = AnimationUtils.loadAnimation(this@LoginActivity, R.anim.shake)
        email_field.startAnimation(shake)
        Toast
            .makeText(this@LoginActivity, getString(R.string.error_invalid_email), Toast.LENGTH_LONG)
            .show()
    }

    private fun passwordError() {
        val shake = AnimationUtils.loadAnimation(this@LoginActivity, R.anim.shake)
        password_field.startAnimation(shake)
        Toast.makeText(this@LoginActivity, getString(R.string.minimum_password), Toast.LENGTH_LONG).show()
    }

    private fun showAuthFailed() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setTitle(getString(R.string.login_failed_title))
            .setMessage(getString(R.string.auth_failed))
            .show()
    }
}