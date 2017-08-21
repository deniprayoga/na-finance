package com.example.denip.nasyiatulaisyiyahfinance

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Created by denip on 8/17/2017.
 */

class FirebaseAuthListener {

    val authListener = FirebaseAuth.AuthStateListener {
        firebaseAuth ->
        val user: FirebaseUser? = firebaseAuth.currentUser

        if (user == null) {

        }
    }
}