package com.example.denip.nasyiatulaisyiyahfinance.expense.category

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast

import com.example.denip.nasyiatulaisyiyahfinance.R
import com.example.denip.nasyiatulaisyiyahfinance.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_category_expense_detail.*

class CategoryExpenseDetailActivity : AppCompatActivity() {

    private var HUNTR = "huntr_CatExpDetAct"
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_expense_detail)

        val intent = intent
        val categoryId = intent.getStringExtra(getString(R.string.CATEGORY_ID_EXPENSE))
        val categoryNumber = intent.getStringExtra(getString(R.string.CATEGORY_NUMBER_EXPENSE))
        val categoryName = intent.getStringExtra(getString(R.string.CATEGORY_NAME_EXPENSE))
        val categoryFirstNumber = intent.getStringExtra(getString(R.string.CATEGORY_FIRST_NUMBER_EXPENSE))
        val categorySecondNumber = intent.getStringExtra(getString(R.string.CATEGORY_SECOND_NUMBER_EXPENSE))
        val categoryThirdNumber = intent.getStringExtra(getString(R.string.CATEGORY_THIRD_NUMBER_EXPENSE))

        category_detail_name_field?.setText(categoryName)
        category_detail_number_first?.setText(categoryFirstNumber).toString()
        category_detail_number_second?.setText(categorySecondNumber)
        category_detail_number_third?.setText(categoryThirdNumber)
        category_detail_number?.text = categoryNumber
        category_detail_id?.text = categoryId

        Log.d(HUNTR, "In the onCreate() event")
        Log.d("hunter_categoryNum", categoryNumber)
        Log.d("hunter_catFirstNumRcv", "" + categoryFirstNumber)
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_category_expense_detail_layout)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_expense_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_bar_save -> {
                var firstNumber = category_detail_number_first?.text.toString()
                var secondNumber = category_detail_number_second?.text.toString()
                var thirdNumber = category_detail_number_third?.text.toString()
                var categoryName = category_detail_name_field?.text.toString()
                when {
                    firstNumber == "" -> showWarningAnimation(category_detail_number_first)
                    firstNumber == "0" -> showWarningFirstNumber(category_detail_number_first)
                    secondNumber == "" -> showWarningAnimation(category_detail_number_second)
                    thirdNumber == "" -> showWarningAnimation(category_detail_number_third)
                    categoryName == "" -> showWarningAnimation(category_detail_name_field)
                    else -> {
                        val categoryId = category_detail_id?.text.toString()
                        val categoryNumber = category_detail_number?.text.toString()
                        firstNumber = category_detail_number_first?.text.toString()
                        secondNumber = category_detail_number_second?.text.toString()
                        thirdNumber = category_detail_number_third?.text.toString()
                        categoryName = category_detail_name_field.text.toString()
                        updateCategory(categoryId, firstNumber.toInt(), secondNumber.toInt(),
                            thirdNumber.toInt(), categoryNumber, categoryName)
                    }
                }
            }
        }
        return true
    }

    private fun showWarningFirstNumber(view: View) {
        showWarningAnimation(view)
        Toast
            .makeText(this@CategoryExpenseDetailActivity,
                getString(R.string.first_number_zero_error),
                Toast.LENGTH_LONG)
            .show()
    }

    private fun showWarningAnimation(view: View) {
        val shake = AnimationUtils.loadAnimation(this@CategoryExpenseDetailActivity, R.anim.shake)
        view.startAnimation(shake)
    }

    override fun onStart() {
        super.onStart()
        Log.d(HUNTR, "In the onStart() event")
        initAuth()
    }

    private fun updateCategory(categoryId: String?,
                               firstNumber: Int?,
                               secondNumber: Int?,
                               thirdNumber: Int?,
                               categoryNumber: String?,
                               categoryName: String?) {
        val dbUpdateCategoryRef = FirebaseDatabase
            .getInstance()
            .getReference("categories/expense")
            .child(categoryId)
        val categoryNumberBind = firstNumber.toString() + "-" + secondNumber.toString() + "-" +
            thirdNumber.toString()
        val category = CategoryExpenseModel(categoryId, firstNumber, secondNumber, thirdNumber,
            categoryNumberBind, categoryName)
        dbUpdateCategoryRef.setValue(category)
        Toast.makeText(this, getString(R.string.category_updated), Toast.LENGTH_SHORT).show()
        finish()

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

    private fun initAuth() {
        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user: FirebaseUser? = firebaseAuth.currentUser

            if (user == null) {
                auth.signOut()
                launchLoginActivity()
                finish()
            }
        }
        auth.addAuthStateListener(authListener)
    }

    private fun launchLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
