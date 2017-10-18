package com.example.denip.nasyiatulaisyiyahfinance.main

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.denip.nasyiatulaisyiyahfinance.expense.ExpenseFragment
import com.example.denip.nasyiatulaisyiyahfinance.expense.ExpenseModel
import com.example.denip.nasyiatulaisyiyahfinance.income.IncomeFragment
import com.example.denip.nasyiatulaisyiyahfinance.login.LoginActivity
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.example.denip.nasyiatulaisyiyahfinance.expense.category.CategoryExpenseSettingActivity
import com.example.denip.nasyiatulaisyiyahfinance.income.category.CategoryIncomeSettingActivity
import com.example.denip.nasyiatulaisyiyahfinance.login.ChangePasswordActivity
import com.example.denip.nasyiatulaisyiyahfinance.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import me.relex.circleindicator.CircleIndicator

class MainActivity : AppCompatActivity(), ExpenseFragment.OnFragmentInteractionListener,
    IncomeFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {
        Log.d(HUNTR, "onFragmentInteraction")

    }

    companion object {
        private val HUNTR = "huntr_MainActivity"
        private var sectionsPagerAdapter: SectionsPagerAdapter? = null
        private var viewPager: ViewPager? = null
        private var indicator: CircleIndicator? = null
        private val auth: FirebaseAuth = FirebaseAuth.getInstance()
        private val databaseExpenseRef = FirebaseDatabase.getInstance().getReference("expenses")
        private var expenses = mutableListOf<ExpenseModel>()
        //private val storage = FirebaseStorage.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewPager()
        initAuth()
        //expenses = ArrayList<ExpenseModel>()
        Log.d(HUNTR + "REF", databaseExpenseRef.toString())

        Log.d(HUNTR, expenses.toString())
        Log.d(HUNTR, "In the onCreate() event")
    }

    override fun onStart() {
        super.onStart()

        Log.d(HUNTR, expenses.toString())
        Log.d(HUNTR, "In the onStart() event")
        initToolbar()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.setting_profile -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.setting_change_password -> startActivity(Intent(this, ChangePasswordActivity::class.java))
            R.id.setting_sign_out -> showDialogSignOut()
            R.id.setting_category_expense -> startActivity(Intent(this, CategoryExpenseSettingActivity::class.java))
            R.id.setting_category_income -> startActivity(Intent(this, CategoryIncomeSettingActivity::class.java))
            R.id.setting_export_expense -> showDialogExportExpense()
            R.id.setting_export_income -> showDialogExportIncome()
        }
        return true
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_main_layout)
    }

    private fun showDialogExportExpense() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setTitle(R.string.confirmation)
            .setMessage(R.string.confirmation_export_expense)
            .setPositiveButton(R.string.yes, { dialog, _ ->
                exportCsv()
                Toast.makeText(this, "Make your export expense action.", Toast.LENGTH_SHORT).show()
            })
            .setNegativeButton(R.string.no, { dialog, _ ->
                dialog.cancel()
            })
            .show()
    }

    private fun showDialogExportIncome() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setTitle(R.string.confirmation)
            .setMessage(R.string.confirmation_export_expense)
            .setPositiveButton(R.string.yes, { dialog, _ ->
                exportCsv()
                Toast.makeText(this, "Make your export expense action.", Toast.LENGTH_SHORT).show()
            })
            .setNegativeButton(R.string.no, { dialog, _ ->
                dialog.cancel()
            })
            .show()
    }

    private fun exportCsv() {

    }

    private fun showDialogSignOut() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setTitle(R.string.confirmation)
            .setMessage(R.string.confirmation_sign_out)
            .setPositiveButton(R.string.yes, { dialog, which ->
                auth.signOut()
                //showLogoutProgress()
            })
            .setNegativeButton(R.string.no, { dialog, _ ->
                dialog.cancel()
            })
            .show()
    }

    private fun initViewPager() {
        Log.d(HUNTR, "In the initViewPager() event")
        sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        viewPager = findViewById(R.id.container) as ViewPager
        indicator = findViewById(R.id.dotsIndicator) as CircleIndicator
        viewPager!!.adapter = sectionsPagerAdapter
        indicator!!.setViewPager(viewPager)

        viewPager!!.currentItem = 1
        Log.d(HUNTR, "viewPager currentItem : " + viewPager!!.currentItem)
    }

    private fun initAuth() {
        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user: FirebaseUser? = firebaseAuth.currentUser

            if (user == null) {
                launchLoginActivity()
            }
        }

        auth.addAuthStateListener(authListener)
    }

    private fun launchLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            Log.d(HUNTR, "In the getItem() event")
            when (position) {
                0 -> return ExpenseFragment()
                2 -> return IncomeFragment()
            }
            Log.d(HUNTR, "position : " + position)
            return MainFragment.newInstance(position)
        }

        override fun getCount(): Int = 3

        override fun getPageTitle(position: Int): CharSequence? {
            Log.d(HUNTR, "In the getPageTitle() event")
            when (position) {
                0 -> return "SECTION 1"
                1 -> return "SECTION 2"
                2 -> return "SECTION 3"
            }
            Log.d(HUNTR, "position : " + position)
            return null
        }
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

    override fun onAttachFragment(fragment: android.app.Fragment?) {
        super.onAttachFragment(fragment)
        Log.d(HUNTR, "In the onAttachFragment()")
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        Log.d(HUNTR, "In the onResumeFragment()")
    }


}
