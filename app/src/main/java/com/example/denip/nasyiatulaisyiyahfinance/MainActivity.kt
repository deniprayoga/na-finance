package com.example.denip.nasyiatulaisyiyahfinance

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var text: String
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text = editText.text.toString()
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()

        //get current user
        //val currentUser = FirebaseAuth.getInstance().currentUser

        val authListener = FirebaseAuth.AuthStateListener {
            firebaseAuth ->
            val user: FirebaseUser? = firebaseAuth.currentUser

            if (user == null) {
                launchLoginActivity()
            }
        }

        sign_out_button.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder
                .setTitle(getString(R.string.confirmation))
                .setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(R.string.yes), { dialog, which ->
                    auth.signOut()
                })
                .setNegativeButton(getString(R.string.no), { dialog, which ->
                    dialog.cancel()
                })
                .show()
        }

        auth.addAuthStateListener(authListener)
    }

    private fun launchLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
