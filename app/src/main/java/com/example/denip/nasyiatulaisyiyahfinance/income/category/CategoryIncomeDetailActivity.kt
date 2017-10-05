package com.example.denip.nasyiatulaisyiyahfinance.income.category

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import com.example.denip.nasyiatulaisyiyahfinance.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_category_income_detail.*

class CategoryIncomeDetailActivity : AppCompatActivity() {

    internal var HUNTER_TAG_LYFCYCL = "hunter_lifecycle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_income_detail)

        val intent = intent
        val categoryId = intent.getStringExtra(getString(R.string.CATEGORY_ID_INCOME))
        val categoryNumber = intent.getStringExtra(getString(R.string.CATEGORY_NUMBER_INCOME))
        val categoryName = intent.getStringExtra(getString(R.string.CATEGORY_NAME_INCOME))
        val categoryFirstNumber = intent.getStringExtra(getString(R.string.CATEGORY_FIRST_NUMBER_INCOME))
        val categorySecondNumber = intent.getStringExtra(getString(R.string.CATEGORY_SECOND_NUMBER_INCOME))
        val categoryThirdNumber = intent.getStringExtra(getString(R.string.CATEGORY_THIRD_NUMBER_INCOME))

        category_detail_name_field?.setText(categoryName)
        category_detail_number_first?.setText(categoryFirstNumber).toString()
        category_detail_number_second?.setText(categorySecondNumber)
        category_detail_number_third?.setText(categoryThirdNumber)
        category_detail_number?.text = categoryNumber
        category_detail_id?.text = categoryId

        Log.d(HUNTER_TAG_LYFCYCL, "In the onCreate() event")
        Log.d("hunter_categoryNum", "" + categoryNumber)
        Log.d("hunter_catFirstNumRcv", "" + categoryFirstNumber)
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_category_income_detail_layout)

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
                    firstNumber == "" -> category_detail_number_first?.error = getString(R.string.prompt_empty_field)
                    firstNumber == "0" -> category_detail_number_first?.error = getString(R.string.first_number_zero_error)
                    secondNumber == "" -> category_detail_number_second?.error = getString(R.string.prompt_empty_field)
                    thirdNumber == "" -> category_detail_number_third?.error = getString(R.string.prompt_empty_field)
                    categoryName == "" -> category_detail_name_field?.error = getString(R.string.prompt_empty_field)
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

    private fun updateCategory(categoryId: String?,
                               firstNumber: Int?,
                               secondNumber: Int?,
                               thirdNumber: Int?,
                               categoryNumber: String?,
                               categoryName: String?) {
        val dbUpdateCategoryRef = FirebaseDatabase
            .getInstance()
            .getReference("categories/income")
            .child(categoryId)
        val categoryNumberBind = firstNumber.toString() + "-" + secondNumber.toString() + "-" +
            thirdNumber.toString()
        val category = CategoryIncomeModel(categoryId, firstNumber, secondNumber, thirdNumber,
            categoryNumberBind, categoryName)
        dbUpdateCategoryRef.setValue(category)
        Toast.makeText(this, getString(R.string.category_updated), Toast.LENGTH_SHORT).show()
        finish()
    }
}
