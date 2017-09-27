package com.example.denip.nasyiatulaisyiyahfinance.expense.category

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.example.denip.nasyiatulaisyiyahfinance.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_expense_category.view.*
import kotlinx.android.synthetic.main.activity_category_expense_setting.*

class CategoryExpenseSettingActivity : AppCompatActivity() {

    companion object {
        internal var TAGGA = "CategoryExpenseSettingActivity"
        private val auth = FirebaseAuth.getInstance()
        private var categories = ArrayList<CategoryModel>()
        private val dbCategoryRef = FirebaseDatabase.getInstance()?.getReference("categories/expense")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_expense_setting)

        initLayout()
        initAuth()
    }

    private fun initAuth() {
        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user: FirebaseUser? = firebaseAuth.currentUser

            if (user == null) {
                finish()
                launchLoginActivity()
            }
        }
        auth.addAuthStateListener(authListener)
    }

    private fun launchLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun initLayout() {
        initToolbar()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAGGA, "In the onStart() event")
        dbCategoryRef?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError?) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                categories.clear()

                dataSnapshot!!.children
                    .map { it.getValue(CategoryModel::class.java) }
                    .forEach { categories.add(it) }

                val categoryAdapter = CategoryListAdapter(this@CategoryExpenseSettingActivity, categories)
                category_expense_list_setting_recycler_view?.adapter = categoryAdapter
            }
        })

        category_expense_list_setting_recycler_view?.layoutManager = LinearLayoutManager(this)
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
        val view: View? = categoryInflater.inflate(R.layout.activity_add_expense_category, null)

        dialogBuilder
            .setView(view)
            .setTitle(getString(R.string.add_expense_category))

        val dialog: AlertDialog = dialogBuilder.create()
        dialog.show()

        view?.button_add_category_expense?.setOnClickListener {
            when {
                view.add_category_number_first?.text!!.isEmpty() ->
                    view.add_category_number_first?.error = getString(R.string.prompt_empty_field)
                view.add_category_number_first.text.toString().toInt() == 0 ->
                    view.add_category_number_first?.error = getString(R.string.first_number_zero_error)
                view.add_category_number_second.text.isEmpty() ->
                    view.add_category_number_second?.error = getString(R.string.prompt_empty_field)
                view.add_category_number_third.text.isEmpty() ->
                    view.add_category_number_third?.error = getString(R.string.prompt_empty_field)
                view.add_category_name_field.text.isEmpty() ->
                    view.add_category_name_field?.error = getString(R.string.prompt_empty_field)
                else -> {
                    val firstNumber = view.add_category_number_first?.text.toString()
                    val secondNumber = view.add_category_number_second?.text.toString().toInt()
                    val thirdNumber = view.add_category_number_third?.text.toString().toInt()
                    val categoryNumber = firstNumber.toString() + "-" + secondNumber.toString() +
                        "-" + thirdNumber.toString()
                    val categoryName = view.add_category_name_field?.text.toString()

                    Log.d("hunter_ini", categoryNumber)
                    Log.d("hunter_categoryName", categoryName)
                    Log.d("hunter_firstNumber", firstNumber.toString())
                    Log.d("hunter_secondNumber", secondNumber.toString())
                    Log.d("hunter_thirdNumber", thirdNumber.toString())

                    val categoryId = dbCategoryRef?.push()?.key
                    val category = CategoryModel(categoryId, firstNumber.toInt(), secondNumber,
                        thirdNumber, categoryNumber, categoryName)
                    dbCategoryRef?.child(categoryId)?.setValue(category)
                    Toast.makeText(this, getString(R.string.category_added), Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_category_setting, menu)
        return true
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("taggg", "In the onRestart() event")
    }

    override fun onResume() {
        super.onResume()
        Log.d("taggg", "In the onResume() event")
    }

    override fun onPause() {
        super.onPause()
        Log.d("taggg", "In the onPause() event")
    }

    override fun onStop() {
        super.onStop()
        Log.d("taggg", "In the onStop() event")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("taggg", "In the onDestroy() event")
    }
}
