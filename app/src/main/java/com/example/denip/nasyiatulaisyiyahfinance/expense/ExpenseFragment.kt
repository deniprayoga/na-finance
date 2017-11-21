package com.example.denip.nasyiatulaisyiyahfinance.expense

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.Toast
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_expense.*
import kotlinx.android.synthetic.main.fragment_expense.view.*
import java.util.ArrayList

class ExpenseFragment : Fragment(), View.OnClickListener {

    companion object {
        private val auth = FirebaseAuth.getInstance()
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        private var expenses: ArrayList<ExpenseModel> = arrayListOf()
        private val databaseExpenseRef = FirebaseDatabase.getInstance().getReference("expenses")
        private var mParam1: String? = null
        private var mParam2: String? = null
        internal var HUNTR = "huntr_ExpenseFragment"
        private var mListener: OnFragmentInteractionListener? = null
        private lateinit var expenseAdapter: ExpenseListAdapter

        fun newInstance(param1: String, param2: String): ExpenseFragment {
            val fragment = ExpenseFragment()
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

        Log.d(HUNTR, "In the onCreate() event")
        initAuth()
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

    override fun onStart() {
        super.onStart()
        Log.d(HUNTR, "In the onStart() event")

        //databaseExpenseRef.orderByKey().startAt("01-10-2017").endAt("31-10-2017")
        /*orderByChild("dateCreated").startAt("01-10-2017").endAt("31-10-2017")*/
        databaseExpenseRef
            /*.orderByChild("dateCreated")
            .startAt("2017-11-01")
            .endAt("2017-11-13")*/
            /*.orderByChild("category")
            .equalTo("7-1-2 Perangko/Pengiriman (Dokumen/Paket)")*/

            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    expenses.clear()

                    dataSnapshot!!.children
                        .map { it.getValue(ExpenseModel::class.java) }
                        .forEach { expenses.add(it) }

                    expenseAdapter = ExpenseListAdapter(activity?.applicationContext, expenses)
                    recycler_view_expense_list?.adapter = expenseAdapter
                }

                override fun onCancelled(databaseError: DatabaseError?) {

                }

            })

        recycler_view_expense_list?.layoutManager = LinearLayoutManager(context)

    }

    fun showExpenseOnlyMe() {
        Log.d(HUNTR, "In the showExpenseOnlyMe() Expense")

        databaseExpenseRef
            .orderByChild("addedByTreasurerUid")
            .equalTo(auth.currentUser?.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    expenses.clear()

                    Log.d(HUNTR, "In the onDataChange() Fragment")

                    dataSnapshot!!.children
                        .map { it.getValue(ExpenseModel::class.java) }
                        .forEach { expenses.add(it!!) }

                    expenseAdapter.notifyDataSetChanged()
                    Log.d(HUNTR, "expense only me itemCount : " + expenseAdapter.itemCount)
                }

                override fun onCancelled(databaseError: DatabaseError?) {

                }

            })
    }

    fun showExpenseAll() {
        databaseExpenseRef
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    expenses.clear()

                    dataSnapshot!!.children
                        .map { it.getValue(ExpenseModel::class.java) }
                        .forEach { expenses.add(it) }

                    expenseAdapter.notifyDataSetChanged()

                    Log.d(HUNTR, "expense all itemCount : " + expenseAdapter.itemCount)
                }

                override fun onCancelled(databaseError: DatabaseError?) {

                }

            })
    }

    private fun showDeletedSuccessfully() {
        Toast.makeText(context, getString(R.string.expense_deleted), Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        Log.d(HUNTR, "In the onResume() event")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(HUNTR, "In the onActivityResult() event")
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(HUNTR, "In the onViewCreated() event")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(HUNTR, "In the onActivityCreated() event")
    }

    override fun onAttachFragment(childFragment: Fragment?) {
        super.onAttachFragment(childFragment)
        Log.d(HUNTR, "In the onAttachFragment() event")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(HUNTR, "In the onDestroyView() event")
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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragmentExpenseView = inflater!!.inflate(R.layout.fragment_expense, container, false)
        fragmentExpenseView.add_expense_button.setOnClickListener(this)
        return fragmentExpenseView
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add_expense_button -> startActivity(Intent(context, AddExpenseActivity::class.java))
        }
    }

    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d(HUNTR, "In the onAttach() event")
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(HUNTR, "In the onDetach() event")
        mListener = null
    }

    interface OnFragmentInteractionListener {

        fun onFragmentInteraction(uri: Uri)
    }
}
