package com.example.denip.nasyiatulaisyiyahfinance.expense

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.example.denip.nasyiatulaisyiyahfinance.login.LoginActivity
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
        private var expenses = ArrayList<ExpenseModel>()
        private val databaseExpenseRef = FirebaseDatabase.getInstance().getReference("expenses")
        private var mParam1: String? = null
        private var mParam2: String? = null
        internal var TAGGA = "ExpenseFragment"
        private var mListener: OnFragmentInteractionListener? = null

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

        Log.d(TAGGA, "In the onCreate() event")
        initAuth()
    }

    private fun initAuth() {
        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user: FirebaseUser? = firebaseAuth.currentUser

            if (user == null) {
                //finish()
                launchLoginActivity()
            }
        }
        auth.addAuthStateListener(authListener)
    }

    private fun launchLoginActivity() {
        startActivity(Intent(context, LoginActivity::class.java))
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAGGA, "In the onStart() event")

        databaseExpenseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                expenses.clear()

                dataSnapshot!!.children
                    .map { it.getValue(ExpenseModel::class.java) }
                    .forEach { expenses.add(it) }

                val expenseAdapter = ExpenseListAdapter(context, expenses)
                recycler_view_expense_list?.adapter = expenseAdapter
            }

            override fun onCancelled(databaseError: DatabaseError?) {

            }
        })

        recycler_view_expense_list?.layoutManager = LinearLayoutManager(context)

    }

    private fun showDeleteDialog(expenseId: String?): Boolean {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder
            .setTitle(getString(R.string.confirmation))
            .setMessage(getString(R.string.delete_expense_message))
            .setPositiveButton(getString(R.string.yes), { dialog, which ->
                val dbDeleteExpenseRef = FirebaseDatabase
                    .getInstance()
                    .getReference("expenses")
                    .child(expenseId)
                dbDeleteExpenseRef.removeValue()
                showDeletedSuccessfully()
            })
            .setNegativeButton(getString(R.string.no), { dialog, which ->
                dialog.dismiss()
            })

        val dialog = dialogBuilder.create()
        dialog.show()
        return true
    }

    private fun showDeletedSuccessfully() {
        Toast.makeText(context, getString(R.string.expense_deleted), Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAGGA, "In the onResume() event")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAGGA, "In the onActivityResult() event")
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAGGA, "In the onViewCreated() event")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAGGA, "In the onActivityCreated() event")
    }

    override fun onAttachFragment(childFragment: Fragment?) {
        super.onAttachFragment(childFragment)
        Log.d(TAGGA, "In the onAttachFragment() event")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAGGA, "In the onDestroyView() event")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAGGA, "In the onPause() event")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAGGA, "In the onStop() event")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAGGA, "In the onDestroy() event")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragmentExpenseView = inflater!!.inflate(R.layout.fragment_expense, container, false)
        fragmentExpenseView.add_expense_button.setOnClickListener(this)
        fragmentExpenseView.export_expense_imageButton?.setOnClickListener(this)
        return fragmentExpenseView
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add_expense_button -> startActivity(Intent(context, AddExpenseActivity::class.java))
            R.id.export_expense_imageButton -> {
                showDialogExport()
            }
        }
    }

    private fun showDialogExport() {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder
            .setTitle(R.string.confirmation)
            .setMessage(R.string.confirmation_export_expense)
            .setPositiveButton(R.string.yes, { dialog, _ ->
                Toast.makeText(context, "Make your export expense action.", Toast.LENGTH_SHORT).show()
            })
            .setNegativeButton(R.string.no, { dialog, _ ->
                dialog.cancel()
            })
            .show()
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
}
