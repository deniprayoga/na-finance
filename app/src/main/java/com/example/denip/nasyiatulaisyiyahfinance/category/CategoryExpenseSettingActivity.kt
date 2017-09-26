package com.example.denip.nasyiatulaisyiyahfinance.category

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_expense_category.*
import kotlinx.android.synthetic.main.activity_add_expense_category.view.*
import kotlinx.android.synthetic.main.activity_category_expense_setting.*

class CategoryExpenseSettingActivity : AppCompatActivity() {

    companion object {
        val categories = mutableListOf<CategoryModel>()
        private val dbCategoryRef = FirebaseDatabase.getInstance()?.getReference("categories/expense")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_expense_setting)

        initLayout()
    }

    private fun initLayout() {
        initToolbar()
    }

    override fun onStart() {
        super.onStart()

        dbCategoryRef?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError?) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {

            }
        })
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_category_expense_setting_layout)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_bar_add_category -> showAddExpenseCategoryDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAddExpenseCategoryDialog() {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val categoryInflater: LayoutInflater = layoutInflater
        val view: View = categoryInflater.inflate(R.layout.activity_add_expense_category, null)

        dialogBuilder.setView(view)
        dialogBuilder.setTitle(getString(R.string.add_expense_category))
        val dialog: AlertDialog = dialogBuilder.create()
        dialog.show()

        view.button_add_category_expense?.setOnClickListener {
            when {
                view.category_number_first?.text!!.isEmpty() ->
                    view.category_number_first?.error = getString(R.string.prompt_empty_field)
                view.category_number_first.text.toString().toInt() == 0 ->
                    view.category_number_first?.error = getString(R.string.first_number_zero_error)
                view.category_number_second.text.isEmpty() ->
                    view.category_number_second?.error = getString(R.string.prompt_empty_field)
                view.category_number_third.text.isEmpty() ->
                    view.category_number_third?.error = getString(R.string.prompt_empty_field)
                view.add_category_name_field.text.isEmpty() ->
                    view.add_category_name_field?.error = getString(R.string.prompt_empty_field)
                else -> {
                    val firstNumber = view.category_number_first?.text.toString().toInt()
                    val secondNumber = view.category_number_second?.text.toString().toInt()
                    val thirdNumber = view.category_number_third?.text.toString().toInt()
                    val categoryNumber = firstNumber.toString() + "-" + secondNumber.toString() +
                        "-" + thirdNumber.toString()
                    val categoryName = view.add_category_name_field?.text.toString()

                    Log.d("hunter_ini", categoryNumber)
                    Log.d("hunter_categoryName", categoryName)
                    Log.d("hunter_firstNumber", firstNumber.toString())
                    Log.d("hunter_secondNumber", secondNumber.toString())
                    Log.d("hunter_thirdNumber", thirdNumber.toString())

                    val categoryId = dbCategoryRef?.push()?.key
                    val category = CategoryModel(categoryId, categoryNumber, categoryName)
                    dbCategoryRef?.child(categoryId)?.setValue(category)
                    Toast.makeText(this, getString(R.string.category_expense_added), Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_category_setting, menu)
        return true
    }
}
