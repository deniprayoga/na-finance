package com.example.denip.nasyiatulaisyiyahfinance.expense.category

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import com.example.denip.nasyiatulaisyiyahfinance.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_category_expense_detail.*

class CategoryExpenseDetailActivity : AppCompatActivity() {

    internal var HUNTER_TAG_LYFCYCL = "hunter_lifecycle"

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

        Log.d(HUNTER_TAG_LYFCYCL, "In the onCreate() event")
        Log.d("hunter_categoryNum", categoryNumber)
        Log.d("hunter_catFirstNumRcv", "" + categoryFirstNumber)
    }

    override fun onStart() {
        super.onStart()
        Log.d(HUNTER_TAG_LYFCYCL, "In the onStart() event")

        button_update_category_detail_expense?.setOnClickListener {
            when {
                category_detail_number_first?.text!!.isEmpty() ->
                    category_detail_number_first?.error = getString(R.string.prompt_empty_field)
                category_detail_number_first.text.toString().toInt() == 0 ->
                    category_detail_number_first?.error = getString(R.string.first_number_zero_error)
                category_detail_number_second.text.isEmpty() ->
                    category_detail_number_second?.error = getString(R.string.prompt_empty_field)
                category_detail_number_third.text.isEmpty() ->
                    category_detail_number_third?.error = getString(R.string.prompt_empty_field)
                category_detail_name_field.text.isEmpty() ->
                    category_detail_name_field?.error = getString(R.string.prompt_empty_field)
                else -> {
                    val categoryId = category_detail_id?.text.toString()
                    val firstNumber = category_detail_number_first?.text.toString().toInt()
                    val secondNumber = category_detail_number_second?.text.toString().toInt()
                    val thirdNumber = category_detail_number_third?.text.toString().toInt()
                    val categoryNumber = category_detail_number?.text.toString()
                    val categoryName = category_detail_name_field.text.toString()
                    updateCategory(categoryId, firstNumber, secondNumber, thirdNumber, categoryNumber, categoryName)
                }
            }
        }
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
        val category = CategoryModel(categoryId, firstNumber, secondNumber, thirdNumber,
            categoryNumberBind, categoryName)
        dbUpdateCategoryRef.setValue(category)
        Toast.makeText(this, getString(R.string.category_updated), Toast.LENGTH_SHORT).show()
        finish()

    }

    override fun onRestart() {
        super.onRestart()
        Log.d(HUNTER_TAG_LYFCYCL, "In the onRestart() event")
    }

    override fun onResume() {
        super.onResume()
        Log.d(HUNTER_TAG_LYFCYCL, "In the onResume() event")
    }

    override fun onPause() {
        super.onPause()
        Log.d(HUNTER_TAG_LYFCYCL, "In the onPause() event")
    }

    override fun onStop() {
        super.onStop()
        Log.d(HUNTER_TAG_LYFCYCL, "In the onStop() event")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(HUNTER_TAG_LYFCYCL, "In the onDestroy() event")
    }
}
