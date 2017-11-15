package com.example.denip.nasyiatulaisyiyahfinance.income.category

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter

import com.example.denip.nasyiatulaisyiyahfinance.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_pick_category_income.*

class PickCategoryIncomeActivity : AppCompatActivity() {

    companion object {
        val dbCategoryRef = FirebaseDatabase.getInstance()?.getReference("categories/income")
        val categories = ArrayList<CategoryIncomeModel>()
        private val HUNTR = "huntr_pckctgryincm"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_category_income)

        initLayout()
    }

    private fun initLayout() {
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_pick_category_income)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        Log.d(HUNTR, "In the onStart() event")

        dbCategoryRef!!.orderByChild("categoryNumber").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                categories.clear()

                dataSnapshot!!.children
                    .map { it.getValue(CategoryIncomeModel::class.java) }
                    .forEach { categories.add(it) }

                val categoryAdapter = PickCategoryIncomeAdapter(this@PickCategoryIncomeActivity, categories)
                pick_category_income_recycler_view?.adapter = categoryAdapter
                pick_category_income_recycler_view?.layoutManager = LinearLayoutManager(this@PickCategoryIncomeActivity)
            }

            override fun onCancelled(databaseErro: DatabaseError?) {

            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(HUNTR, "In the onRestart() event")
    }

    override fun onResume() {
        super.onResume()
        Log.d(HUNTR, "In the onResume() event")
    }

    override fun onPause() {
        super.onPause()
        Log.d(HUNTR, "In the onPause() event")
    }

    override fun onStop() {
        super.onStop()
        Log.d(HUNTR, "In the onStop() event")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(HUNTR, "In the onDestroy() event")
    }
}
