package com.example.denip.nasyiatulaisyiyahfinance

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        checkCurrentUser()
        initLayout()

    }

    private fun initLayout() {
        forgot_password_text_view.setOnClickListener(this)
        initSignInButton()
    }

    private fun initSignInButton() {
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.forgot_password_text_view -> launchForgotPasswordActivity()
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
    }

    private fun hideLoginProgress() {
        login_progress.visibility = View.GONE
    }

    private fun launchMainActivity() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    private fun showEmptyEmail() {
        Toast.makeText(applicationContext, getString(R.string.prompt_enter_email), Toast.LENGTH_LONG).show()
    }

    private fun showEmptyPassword() {
        Toast.makeText(applicationContext, getString(R.string.prompt_enter_password), Toast.LENGTH_LONG).show()
    }

    private fun emailError() {
        email_field.error = getString(R.string.error_invalid_email)
    }

    private fun passwordError() {
        password_field.error = getString(R.string.minimum_password)
    }

    private fun showAuthFailed() {
        Toast.makeText(applicationContext, getString(R.string.auth_failed), Toast.LENGTH_LONG).show()
    }
}