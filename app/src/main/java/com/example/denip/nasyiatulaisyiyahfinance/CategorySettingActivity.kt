package com.example.denip.nasyiatulaisyiyahfinance

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_category_setting.*

class CategorySettingActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_setting)

        initLayout()
    }

    private fun initLayout() {
        initToolbar()

    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_category_setting_layout)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(v: View?) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> onBackPressed()
        }

        return true
    }

}
