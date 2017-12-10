package com.example.denip.nasyiatulaisyiyahfinance.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : AppCompatActivity(), View.OnClickListener {

    private val auth = FirebaseAuth.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        initToolbar()
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            R.id.action_bar_save -> changePassword()
        }
        return true
    }

    private fun changePassword() {
        val userEmail = user?.email.toString()
        val oldPassword = old_password_field.text.toString().trim()
        val newPassword = new_password_field.text.toString().trim()


        when {
            oldPassword.isEmpty() -> showWarningAnimation(old_password_field)
            newPassword.isEmpty() -> showWarningAnimation(new_password_field)
            else -> {
                val credential = EmailAuthProvider.getCredential(userEmail, oldPassword)
                user?.reauthenticate(credential)?.addOnCompleteListener { task ->

                    when {
                        task.isSuccessful -> {
                            user.updatePassword(newPassword).addOnCompleteListener { task_ ->
                                when {
                                    task_.isSuccessful -> {
                                        Toast.makeText(this, getString(R.string.password_updated),
                                            Toast.LENGTH_SHORT).show()
                                        auth.signOut()
                                    }
                                    else -> {
                                        Toast.makeText(this, getString(R.string.password_failed_update),
                                            Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                        else -> {
                            Toast.makeText(this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

    }

    private fun showWarningAnimation(view: View) {
        val shake = AnimationUtils.loadAnimation(this@ChangePasswordActivity, R.anim.shake)
        view.startAnimation(shake)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_change_password, menu)
        return true
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_change_password_layout)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        initAuth()
    }

    private fun initAuth() {
        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user: FirebaseUser? = firebaseAuth.currentUser

            if (user == null) {
                auth.signOut()
                launchLoginActivity()
                finish()
            }
        }

        auth.addAuthStateListener(authListener)
    }

    private fun launchLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
