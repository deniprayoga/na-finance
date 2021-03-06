package com.example.denip.nasyiatulaisyiyahfinance.income.category

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_income_category.view.*
import kotlinx.android.synthetic.main.activity_category_income_setting.*

class CategoryIncomeSettingActivity : AppCompatActivity() {

    companion object {
        private var categories = ArrayList<CategoryIncomeModel>()
        private val auth = FirebaseAuth.getInstance()
        private val dbCategoryRef = FirebaseDatabase.getInstance().getReference("categories/income")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_income_setting)

        initLayout()
        initAuth()
        Log.d("taggg", "In the onCreate() event")
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

    private fun initToolbar() {
        setSupportActionBar(toolbar_category_income_setting_layout)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_category_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_bar_add_category -> showAddIncomeCategoryDialog()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAddIncomeCategoryDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val categoryInflater = layoutInflater
        val view = categoryInflater.inflate(R.layout.activity_add_income_category, null)

        dialogBuilder
            .setView(view)
            .setTitle(getString(R.string.add_category))

        val dialog = dialogBuilder.create()
        dialog.show()

        view?.button_add_category_income?.setOnClickListener {
            when {
                view.add_category_number_first?.text!!.isEmpty() ->
                    showWarningAnimation(view.add_category_number_first)
                view.add_category_number_first.text.toString().toInt() == 0 ->
                    showWarningFirstNumber(view.add_category_number_first)
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

                    Log.d("hunter_ini", categoryNumber)
                    Log.d("hunter_categoryName", categoryName)
                    Log.d("hunter_firstNumber", firstNumber.toString())
                    Log.d("hunter_secondNumber", secondNumber.toString())
                    Log.d("hunter_thirdNumber", thirdNumber.toString())

                    val categoryId = dbCategoryRef?.push()?.key
                    val category = CategoryIncomeModel(categoryId, firstNumber.toInt(), secondNumber,
                        thirdNumber, categoryNumber, categoryName)
                    dbCategoryRef?.child(categoryId)?.setValue(category)
                    Toast.makeText(this, getString(R.string.category_added), Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        }

    }

    private fun showWarningFirstNumber(view: View) {
        showWarningAnimation(view)
        Toast
            .makeText(this@CategoryIncomeSettingActivity,
                getString(R.string.first_number_zero_error),
                Toast.LENGTH_LONG)
            .show()
    }

    private fun showWarningAnimation(view: View) {
        val shake = AnimationUtils.loadAnimation(this@CategoryIncomeSettingActivity, R.anim.shake)
        view.startAnimation(shake)
    }

    override fun onStart() {
        super.onStart()
        Log.d("taggg", "In the onStart() event")

        dbCategoryRef!!
            .orderByChild("categoryNumber")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    categories.clear()

                    dataSnapshot!!.children
                        .map { it.getValue(CategoryIncomeModel::class.java) }
                        .forEach { categories.add(it) }
                    val categoryAdapter = CategoryIncomeListAdapter(this@CategoryIncomeSettingActivity, categories)
                    category_income_list_setting_recycler_view?.adapter = categoryAdapter
                }

                override fun onCancelled(databaseError: DatabaseError?) {

                }
            })
        category_income_list_setting_recycler_view?.layoutManager = LinearLayoutManager(this)
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
