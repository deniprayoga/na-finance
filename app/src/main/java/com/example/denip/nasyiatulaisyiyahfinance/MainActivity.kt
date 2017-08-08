package com.example.denip.nasyiatulaisyiyahfinance

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //get firebase auth instance
        val auth = FirebaseAuth.getInstance()

        //get current user
        val currentUser = FirebaseAuth.getInstance().currentUser

        val authListener = FirebaseAuth.AuthStateListener {
            firebaseAuth ->
            val user:FirebaseUser? = firebaseAuth.currentUser

            if (user==null){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        sign_out_button.setOnClickListener {
            auth.signOut()
        }

        auth.addAuthStateListener(authListener)
    }
}
