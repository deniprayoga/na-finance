package com.example.denip.nasyiatulaisyiyahfinance

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        checkCurrentUser()
        showOrHidePassword()

        sign_in_button.setOnClickListener {
            val email = email_field.text.toString()
            val password = password_field.text.toString()

            if (TextUtils.isEmpty(email)) {
                showEmptyEmail()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                showEmptyPassword()
                return@setOnClickListener
            }

            showLoginProgress()

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
    }

    private fun showOrHidePassword() {
        show_password_checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) password_field.transformationMethod = PasswordTransformationMethod.getInstance()
            else password_field.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }
    }

    private fun checkCurrentUser() {
        if (auth.currentUser != null) {
            launchMainActivity()
        }
    }

    private fun showLoginProgress() {
        login_progress.visibility = View.VISIBLE
    }

    private fun hideLoginProgress() {
        login_progress.visibility = View.GONE
    }

    private fun launchMainActivity() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    private fun showEmptyEmail() {
        Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_LONG).show()
    }

    private fun showEmptyPassword() {
        Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_LONG).show()
    }

    private fun emailError() {
        email_field.error = getString(R.string.error_invalid_email)
    }

    private fun passwordError() {
        password_field.error = getString(R.string.minimum_password)
    }

    private fun showAuthFailed() {
        Toast.makeText(applicationContext, "auth failed", Toast.LENGTH_LONG).show()
    }
}