package com.example.denip.nasyiatulaisyiyahfinance.expense

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_expense.*
import kotlinx.android.synthetic.main.fragment_expense.view.*
import java.util.ArrayList

class ExpenseFragment : Fragment(), View.OnClickListener {

    companion object {

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        private var expenses = mutableListOf<ExpenseModel>()
        private val databaseExpenseRef = FirebaseDatabase.getInstance().getReference("expenses")

        fun newInstance(param1: String, param2: String): ExpenseFragment {
            val fragment = ExpenseFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    private var mParam1: String? = null
    private var mParam2: String? = null
    internal var TAGGA = "ExpenseFragment"

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }

        Log.d(TAGGA, "In the onCreate() event")
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

                val expenseAdapter = ExpenseList(activity, expenses)
                listViewExpensesF.adapter = expenseAdapter
            }

            override fun onCancelled(databaseError: DatabaseError?) {

            }
        })

        expenses = ArrayList()

        listViewExpensesF?.setOnItemClickListener { parent, view, position, id ->
            val expense = expenses[position]
            val intent = Intent(context, ExpenseDetailActivity::class.java)

            intent.putExtra(getString(R.string.EXPENSE_ID), expense.expenseId)
            intent.putExtra(getString(R.string.EXPENSE_NOTE), expense.note)
            intent.putExtra(getString(R.string.EXPENSE_AMOUNT), expense.amount.toString())
            intent.putExtra(getString(R.string.EXPENSE_CATEGORY), expense.category)
            intent.putExtra(getString(R.string.EXPENSE_DATE), expense.dateCreated)
            intent.putExtra(getString(R.string.EXPENSE_NOTE_PHOTO_URI), expense.notePhotoUri)

            startActivity(intent)
        }

        listViewExpensesF?.setOnItemLongClickListener { parent, view, position, id ->
            val expense = expenses[position]
            Toast.makeText(context, expense.amount.toString(), Toast.LENGTH_SHORT).show()
            true
        }
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