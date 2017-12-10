package com.example.denip.nasyiatulaisyiyahfinance.home

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.example.denip.nasyiatulaisyiyahfinance.expense.ExpenseListAdapter
import com.example.denip.nasyiatulaisyiyahfinance.expense.ExpenseModel
import com.example.denip.nasyiatulaisyiyahfinance.income.IncomeListAdapter
import com.example.denip.nasyiatulaisyiyahfinance.income.IncomeModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.text.NumberFormat
import java.util.*

class HomeFragment : Fragment() {

    companion object {
        private val auth = FirebaseAuth.getInstance()
        private var mParam1: String? = null
        private var mParam2: String? = null
        private var mListener: OnFragmentInteractionListener? = null
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        private val databaseExpenseRef = FirebaseDatabase.getInstance().getReference("expenses")
        private val databaseIncomeRef = FirebaseDatabase.getInstance().getReference("incomes")
        internal var HUNTR = "huntr_HomeFragment"
        private lateinit var expenseAdapter: ExpenseListAdapter
        private lateinit var incomeAdapter: IncomeListAdapter
        private var expenses: ArrayList<ExpenseModel> = arrayListOf()
        private var incomes: ArrayList<IncomeModel> = arrayListOf()
        private val dbRef = FirebaseDatabase.getInstance().reference
        private var expenseTotalToBalance = 0
        private var incomeTotalToBalance = 0

        fun newInstance(param1: String, param2: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.d(HUNTR, "In the onCreateView()")
        val view = inflater!!.inflate(R.layout.fragment_home, container, false)
        view.balance_text.isCursorVisible = false
        view.balance_text.isFocusable = false
        view.expense_total_text.isCursorVisible = false
        view.expense_total_text.isFocusable = false
        view.income_total_text.isCursorVisible = false
        view.income_total_text.isFocusable = false
        view.rp_balance_text.isCursorVisible = false
        view.rp_balance_text.isFocusable = false

        return view
    }

    override fun onStart() {
        super.onStart()
        Log.d(HUNTR, "In the onStart()")
        initAuth()

        expenseAdapter = ExpenseListAdapter(activity?.applicationContext, expenses)
        recycler_view_expense_list_home?.adapter = expenseAdapter
        recycler_view_expense_list_home?.layoutManager = LinearLayoutManager(context)

        incomeAdapter = IncomeListAdapter(activity?.applicationContext, incomes)
        recycler_view_income_list_home?.adapter = incomeAdapter
        recycler_view_income_list_home?.layoutManager = LinearLayoutManager(context)

        getIncomeTotal()
        getExpenseTotal()
        showLatestExpense()
    }

    private fun getIncomeTotal() {
        Log.d(HUNTR, "In the getIncomeTotal()")

        dbRef.child("incomes").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError?) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                Log.d(HUNTR, "In the onDataChange getIncomeTotal()")
                var incomeTotal = 0
                for (postSnapshot in dataSnapshot!!.children) {
                    val incomes = postSnapshot.getValue(IncomeModel::class.java)
                    incomeTotal += incomes.amount!!
                }
                Log.d(HUNTR, "totalIncome value : " + incomeTotal)
                incomeTotalToBalance = incomeTotal
                Log.d(HUNTR, "incomeTotalToBalance in getIncomeTotal : " + incomeTotalToBalance)
                val formattedAmount = formatAmount(incomeTotalToBalance)
                getBalance()
                income_total_text?.setText(formattedAmount)
            }
        })
    }

    private fun getExpenseTotal() {
        Log.d(HUNTR, "In the getExpenseTotal()")
        dbRef.child("expenses").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError?) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                Log.d(HUNTR, "In the onDataChange getExpenseTotal()")
                var expenseTotal = 0
                for (postSnapshot in dataSnapshot!!.children) {
                    val expenses = postSnapshot.getValue(ExpenseModel::class.java)
                    expenseTotal += expenses.amount!!
                }
                Log.d(HUNTR, "totalExpense value : " + expenseTotal)
                expenseTotalToBalance = expenseTotal
                Log.d(HUNTR, "expenseTotalToBalance in getExpenseTotal : " + expenseTotalToBalance)
                val formattedAmount = formatAmount(expenseTotalToBalance)
                getBalance()
                expense_total_text?.setText(formattedAmount)
            }
        })
    }

    private fun getBalance() {
        Log.d(HUNTR, "in the getBalance()")
        Log.d(HUNTR, "incomeTotalToBalance : " + incomeTotalToBalance)
        Log.d(HUNTR, "expenseTotalToBalance : " + expenseTotalToBalance)
        val balance = incomeTotalToBalance - expenseTotalToBalance
        val formattedBalance = formatAmount(balance)
        Log.d(HUNTR, "balance : " + balance)
        view?.balance_text?.setText(formattedBalance)
    }

    private fun formatAmount(amount: Int): String =
        NumberFormat.getNumberInstance(Locale.US).format(amount)

    private fun showLatestExpense() {
        databaseExpenseRef.limitToLast(3).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError?) {

            }

            override fun onDataChange(dataSnapshotExpenses: DataSnapshot?) {
                databaseIncomeRef.limitToLast(3).addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(databaseError: DatabaseError?) {

                    }

                    override fun onDataChange(dataSnapshotIncomes: DataSnapshot?) {
                        expenses.clear()
                        incomes.clear()

                        dataSnapshotExpenses!!.children
                            .map { it.getValue(ExpenseModel::class.java) }
                            .forEach { expenses.add(it) }

                        dataSnapshotIncomes!!.children
                            .map { it.getValue(IncomeModel::class.java) }
                            .forEach { incomes.add(it) }

                        expenseAdapter.notifyDataSetChanged()
                        incomeAdapter.notifyDataSetChanged()
                    }
                })
            }
        })
    }

    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {

        fun onFragmentInteraction(uri: Uri)
    }

    private fun initAuth() {
        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user: FirebaseUser? = firebaseAuth.currentUser

            if (user == null) {
                auth.signOut()
            }
        }
        auth.addAuthStateListener(authListener)
    }
}
