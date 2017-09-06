package com.example.denip.nasyiatulaisyiyahfinance

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initLayout()

        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user: FirebaseUser? = firebaseAuth.currentUser

            if (user == null) {
                launchLoginActivity()
            }
        }
        auth.addAuthStateListener(authListener)
    }

    private fun launchLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }*/

    private fun initLayout() {
        initToolbar()
        setting_profile.setOnClickListener(this)
        setting_category.setOnClickListener(this)
        setting_change_password.setOnClickListener(this)
        setting_sign_out.setOnClickListener(this)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_setting_layout)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.setting_profile -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.setting_category -> startActivity(Intent(this, CategorySettingActivity::class.java))
            R.id.setting_change_password -> startActivity(Intent(this, ChangePasswordActivity::class.java))
            R.id.setting_sign_out -> {
                showDialogSignOut()
            }
        }
    }

    private fun showDialogSignOut() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setTitle(R.string.confirmation)
            .setMessage(R.string.confirmation_sign_out)
            .setPositiveButton(R.string.yes, { dialog, which ->
                auth.signOut()
            })
            .setNegativeButton(R.string.no, { dialog, _ ->
                dialog.cancel()
            })
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}
