package com.example.denip.nasyiatulaisyiyahfinance

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initLayout()
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }*/

    private fun initLayout() {
        initToolbar()
        profile_button.setOnClickListener(this)
        category_setting_button.setOnClickListener(this)
        sign_out_button.setOnClickListener(this)
    }

    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar_setting_layout) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.profile_button -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.category_setting_button -> startActivity(Intent(this, CategorySettingActivity::class.java))
            R.id.sign_out_button -> {

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}
