package com.example.denip.nasyiatulaisyiyahfinance

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_login.*

class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        initLayout()
    }

    private fun initLayout() {
        reset_password_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.reset_password_button -> resetPassword()
        }
    }

    private fun resetPassword() {
        val email = email_field_forgot_password?.text.toString()

        if (TextUtils.isEmpty(email)) {
            showEmptyEmail()
        } else {
            showResetPasswordProgress()

            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                hideResetPasswordProgress()
                if (!email.contains(getString(R.string.contain_at_gmail))) {
                    emailError()
                } else if (task.isSuccessful) {
                    showDialogResetPasswordSuccess()
                } else {
                    showDialogResetPasswordFailed()
                }
            }
        }
    }

    private fun showEmptyEmail() {
        email_field_forgot_password?.error = getString(R.string.prompt_enter_email)
    }


    private fun showDialogResetPasswordSuccess() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setTitle(getString(R.string.reset_password_success))
            .setMessage(getString(R.string.reset_password_success_message))
            .setPositiveButton(getString(R.string.ok), { dialog, _ ->
                dialog.dismiss()
                finish()
            })
            .show()
        email_field?.setText("")
        password_field?.setText("")
    }

    private fun showDialogResetPasswordFailed() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setTitle(getString(R.string.reset_password_failed))
            .setMessage(getString(R.string.reset_password_failed_message))
            .setPositiveButton(getString(R.string.ok), { dialog, _ ->
                dialog.dismiss()
            })
            .show()
    }

    private fun emailError() {
        email_field_forgot_password.error = getString(R.string.error_invalid_email)
    }

    private fun showResetPasswordProgress() {
        reset_password_progress.visibility = View.VISIBLE
        reset_password_button.isEnabled = false
    }

    private fun hideResetPasswordProgress() {
        reset_password_progress.visibility = View.GONE
        reset_password_button.isEnabled = true
    }
}
