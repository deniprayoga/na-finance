package com.example.denip.nasyiatulaisyiyahfinance.expense.category

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
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
        internal var HUNTR = "huntr_CatExpSetAct"
        private val auth = FirebaseAuth.getInstance()
        private var categories = ArrayList<CategoryExpenseModel>()
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
        Log.d(HUNTR, "In the CategoryExpenseSettingActivity()")
    }

    override fun onStart() {
        super.onStart()
        Log.d(HUNTR, "In the onStart() event")
        dbCategoryRef!!
            .orderByChild("categoryNumber")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError?) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    categories.clear()

                    dataSnapshot!!.children
                        .map { it.getValue(CategoryExpenseModel::class.java) }
                        .forEach { categories.add(it) }

                    val categoryAdapter = CategoryExpenseListAdapter(this@CategoryExpenseSettingActivity, categories)
                    category_expense_list_setting_recycler_view?.adapter = categoryAdapter
                }
            })

        category_expense_list_setting_recycler_view?.layoutManager = LinearLayoutManager(this@CategoryExpenseSettingActivity)
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
        Log.d(HUNTR, "showDialogExpenseCategoryDialog()")
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
                    showWarningAnimation(view.add_category_number_first)
                view.add_category_number_first.text.toString().toInt() == 0 ->
                    Toast
                        .makeText(this@CategoryExpenseSettingActivity,
                            getString(R.string.first_number_zero_error),
                            Toast.LENGTH_LONG)
                        .show()
                view.add_category_number_second.text.isEmpty() ->
                    showWarningAnimation(view.add_category_number_second)
                view.add_category_number_third.text.isEmpty() ->
                    showWarningAnimation(view.add_category_number_third)
                view.add_category_name_field.text.isEmpty() ->
                    showWarningAnimation(view.add_category_name_field)
                else -> {
                    val firstNumber = view.add_category_number_first?.text.toString()
                    val secondNumber = view.add_category_number_second?.text.toString().toInt()
                    val thirdNumber = view.add_category_number_third?.text.toString().toInt()
                    val categoryNumber = firstNumber.toString() + "-" + secondNumber.toString() +
                        "-" + thirdNumber.toString()
                    val categoryName = view.add_category_name_field?.text.toString()

                    Log.d(HUNTR, categoryNumber)
                    Log.d(HUNTR, categoryName)
                    Log.d(HUNTR, firstNumber.toString())
                    Log.d(HUNTR, secondNumber.toString())
                    Log.d(HUNTR, thirdNumber.toString())
                    val categoryId = dbCategoryRef?.push()?.key
                    val category = CategoryExpenseModel(categoryId, firstNumber.toInt(), secondNumber,
                        thirdNumber, categoryNumber, categoryName)
                    dbCategoryRef?.child(categoryId)?.setValue(category)
                    Toast.makeText(this, getString(R.string.category_added), Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        }
    }

    private fun showWarningAnimation(view: View) {
        val shake = AnimationUtils.loadAnimation(this@CategoryExpenseSettingActivity, R.anim.shake)
        view.startAnimation(shake)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_category_setting, menu)
        Log.d(HUNTR, "In the onCreateOptionsMenu()")
        return true
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
