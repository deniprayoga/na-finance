package com.example.denip.nasyiatulaisyiyahfinance.main

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.denip.nasyiatulaisyiyahfinance.expense.ExpenseDetailActivity
import com.example.denip.nasyiatulaisyiyahfinance.expense.ExpenseFragment
import com.example.denip.nasyiatulaisyiyahfinance.expense.ExpenseList
import com.example.denip.nasyiatulaisyiyahfinance.expense.ExpenseModel
import com.example.denip.nasyiatulaisyiyahfinance.income.IncomeFragment
import com.example.denip.nasyiatulaisyiyahfinance.login.LoginActivity
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import me.relex.circleindicator.CircleIndicator

class MainActivity : AppCompatActivity(), ExpenseFragment.OnFragmentInteractionListener,
    IncomeFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {
        Log.d(TAGGG, "onFragmentInteraction")

    }

    companion object {
        private val TAGGG = "MainActivity"
        private var sectionsPagerAdapter: SectionsPagerAdapter? = null
        private var viewPager: ViewPager? = null
        private var indicator: CircleIndicator? = null
        private val auth: FirebaseAuth = FirebaseAuth.getInstance()
        private val databaseExpenseRef = FirebaseDatabase.getInstance().getReference("expenses")
        private var expenses = mutableListOf<ExpenseModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewPager()
        initAuth()
        expenses = ArrayList()
        Log.d("reference", databaseExpenseRef.toString())

        listViewExpenses?.setOnItemClickListener { parent, view, position, id ->
            val expense = expenses[position]
            val intent = Intent(applicationContext, ExpenseDetailActivity::class.java)

            intent.putExtra(getString(R.string.EXPENSE_ID), expense.expenseId)
            intent.putExtra(getString(R.string.EXPENSE_NOTE), expense.note)
            intent.putExtra(getString(R.string.EXPENSE_AMOUNT), expense.amount.toString())
            intent.putExtra(getString(R.string.EXPENSE_CATEGORY), expense.category)
            intent.putExtra(getString(R.string.EXPENSE_DATE), expense.dateCreated)
            intent.putExtra(getString(R.string.EXPENSE_NOTE_PHOTO_URI), expense.notePhotoUri)
            Log.d("inteee", expense.amount.toString())
            startActivity(intent)
        }
        
        listViewExpenses?.setOnItemLongClickListener { parent, view, position, id ->
            val expense = expenses[position]
            Toast.makeText(this, expense.amount.toString(), Toast.LENGTH_SHORT).show()
            true
        }

        Log.d("exxx", expenses.toString())
        Log.d(TAGGG, "In the onCreate() event")
    }

    override fun onStart() {
        super.onStart()

        databaseExpenseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                expenses.clear()
                //val expense = dataSnapshot!!.getValue(ExpenseModel::class.java)
                //expenses.add(expense)
                for (postSnapshot: DataSnapshot in dataSnapshot!!.children) {
                    val expense = postSnapshot.getValue(ExpenseModel::class.java)
                    expenses.add(expense)
                }

                val expenseAdapter = ExpenseList(this@MainActivity, expenses)

                listViewExpenses.adapter = expenseAdapter
            }

            override fun onCancelled(databaseError: DatabaseError?) {

            }
        })
        Log.d("expensse", expenses.toString())
        Log.d(TAGGG, "In the onStart() event")
    }

    private fun initViewPager() {
        sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        viewPager = findViewById(R.id.container) as ViewPager
        indicator = findViewById(R.id.dotsIndicator) as CircleIndicator
        viewPager!!.adapter = sectionsPagerAdapter
        indicator!!.setViewPager(viewPager)

        viewPager!!.currentItem = 1
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
            when (position) {
                0 -> return ExpenseFragment()
                2 -> return IncomeFragment()
            }
            return MainFragment.newInstance(position)
        }

        override fun getCount(): Int = 3

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "SECTION 1"
                1 -> return "SECTION 2"
                2 -> return "SECTION 3"
            }
            return null
        }
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAGGG, "In the onRestart() event")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAGGG, "In the onResume() event")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAGGG, "In the onPause() event")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAGGG, "In the onStop() event")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAGGG, "In the onDestroy() event")
    }
}
