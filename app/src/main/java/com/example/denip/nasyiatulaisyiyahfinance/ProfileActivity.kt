package com.example.denip.nasyiatulaisyiyahfinance

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    val context : Context? = null
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

        edit_profile_button.setOnClickListener(this)
        save_profile_button.setOnClickListener(this)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.edit_profile_button -> {
                profile_name_field.isEnabled = true
                phone_number_profile_field.isEnabled = true
                location_profile_field.isEnabled = true
                edit_profile_button.visibility = View.INVISIBLE
                save_profile_button.visibility = View.VISIBLE
            }
            R.id.save_profile_button -> {
                profile_name_field.isEnabled = false
                phone_number_profile_field.isEnabled = false
                location_profile_field.isEnabled = false
                edit_profile_button.visibility = View.VISIBLE
                save_profile_button.visibility = View.INVISIBLE
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> onBackPressed()
            R.id.action_bar_edit_profile -> {
                profile_name_field.isEnabled = true
                phone_number_profile_field.isEnabled = true
                location_profile_field.isEnabled = true
            }
            R.id.action_bar_save_profile -> {
                profile_name_field.isEnabled = false
                phone_number_profile_field.isEnabled = false
                location_profile_field.isEnabled = false
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
