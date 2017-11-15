package com.example.denip.nasyiatulaisyiyahfinance.expense.category

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem

import com.example.denip.nasyiatulaisyiyahfinance.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_pick_category_expense.*

class PickCategoryExpenseActivity : AppCompatActivity() {

    companion object {
        private val HUNTR = "huntr_PickCategoryXpnse"
        val dbCategoryRef = FirebaseDatabase.getInstance()?.getReference("categories/expense")
        val categories = ArrayList<CategoryExpenseModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_category_expense)

        initLayout()
        Log.d(HUNTR, "In the onCreate() event")
    }

    private fun initLayout() {
        initToolbar()
    }

    override fun onStart() {
        super.onStart()
        Log.d(HUNTR, "In the onStart() event")
        dbCategoryRef!!.orderByChild("categoryNumber").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError?) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                hideCursorOnNoteField()
                categories.clear()

                dataSnapshot!!.children
                    .map { it.getValue(CategoryExpenseModel::class.java) }
                    .forEach { categories.add(it) }

                val categoryAdapter = PickCategoryExpenseAdapter(this@PickCategoryExpenseActivity, categories)
                pick_category_expense_recycler_view?.adapter = categoryAdapter

                pick_category_expense_recycler_view?.layoutManager = LinearLayoutManager(this@PickCategoryExpenseActivity)

            }
        })
    }

    private fun hideCursorOnNoteField() {

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_pick_category_expense)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
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

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d(HUNTR, "In the onBackPressed() event")

    }
}
