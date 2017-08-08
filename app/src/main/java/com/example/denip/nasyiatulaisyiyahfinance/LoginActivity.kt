package com.example.denip.nasyiatulaisyiyahfinance

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //get firebase auth instance
        val auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        sign_in_button.setOnClickListener {
            val email = email_field.text.toString()
            val password = password_field.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            login_progress.visibility = View.VISIBLE

            //authenticate user
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                login_progress.visibility = View.GONE
                if (!task.isSuccessful) {
                    if (password.length < 6) {
                        password_field.error = getString(R.string.minimum_password)
                    } else {
                        Toast.makeText(applicationContext, "auth failed", Toast.LENGTH_LONG).show()
                    }
                } else {
                    //val intent: Intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

}