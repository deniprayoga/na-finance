package com.example.denip.nasyiatulaisyiyahfinance

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initLayout()
    }

    private fun initLayout() {
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_profile_layout)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}
