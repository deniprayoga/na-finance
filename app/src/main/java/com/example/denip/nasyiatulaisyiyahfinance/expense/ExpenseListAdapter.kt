package com.example.denip.nasyiatulaisyiyahfinance.expense

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

/**
 * Created by denip on 9/25/2017.
 */

class ExpenseListAdapter(context: Context?, expenses: ArrayList<ExpenseModel>) :
    RecyclerView.Adapter<ExpenseListAdapter.CustomViewHolder>() {
    companion object {
        private val auth = FirebaseAuth.getInstance()
        private val HUNTR = "huntr_ExpenseListAdptr"
    }

    private var context: Context? = context
    private var expenses: ArrayList<ExpenseModel> = arrayListOf()

    init {
        this.expenses = expenses
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        val expense = expenses[position]
        holder!!.note.text = expense.note.toString().replace("^", "")
        holder.category.text = expense.category.toString().replace("^", "")
        holder.amount.text = "Rp ${expense.amount.toString().replace("^", "")}"
        holder.dateCreated.text = expense.dateCreated.toString().replace("^", "")
        holder.addedBy.text = expense.addedByTreasurer.toString().replace("^", "")
        holder.categoryId.text = expense.categoryId.toString().replace("^", "")
        holder.addedByInitial.text = expense.addedByTreasurerInitial.toString()
    }

    override fun getItemCount(): Int = expenses.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.expense_list_row_view, parent, false)
        return CustomViewHolder(view)
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val addedBy = view.findViewById(R.id.expense_list_row_view_added_by) as TextView
        val amount = view.findViewById(R.id.expense_list_row_view_amount) as TextView
        val category = view.findViewById(R.id.expense_list_row_view_category) as TextView
        val dateCreated = view.findViewById(R.id.expense_list_row_view_date) as TextView
        val note = view.findViewById(R.id.expense_list_row_view_note) as TextView
        val categoryId = view.findViewById(R.id.expense_list_row_view_category_id) as TextView
        val addedByInitial = view.findViewById(R.id.initial_field) as TextView

        init {
            view.setOnClickListener { v ->
                Log.d(HUNTR, "-----------------in setOnClickListener()------------------")
                val dbRef = FirebaseDatabase.getInstance().getReference("users")
                dbRef.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(databaseError: DatabaseError?) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        val fullName = dataSnapshot?.child(auth.currentUser?.uid)?.child("fullName")?.value.toString()

                        val initial = dataSnapshot?.child(auth.currentUser?.uid)?.child("initial")?.value.toString()
                        val expense = expenses[adapterPosition]
                        notifyDataSetChanged()
                        val intent = Intent(v?.context, ExpenseDetailActivity::class.java)
                        intent.putExtra(context?.getString(R.string.EXPENSE_ID), expense.expenseId)
                        intent.putExtra(context?.getString(R.string.EXPENSE_NOTE), expense.note.toString().replace("^", ""))
                        intent.putExtra(context?.getString(R.string.EXPENSE_AMOUNT), expense.amount.toString().replace("^", ""))
                        intent.putExtra(context?.getString(R.string.EXPENSE_CATEGORY), expense.category.toString().replace("^", ""))
                        intent.putExtra(context?.getString(R.string.EXPENSE_DATE), expense.dateCreated.toString().replace("^", ""))
                        intent.putExtra(context?.getString(R.string.EXPENSE_ADDED_BY_TREASURER_INITIAL), expense.addedByTreasurerInitial)
                        intent.putExtra(context?.getString(R.string.EXPENSE_ADDED_BY_TREASURER), fullName.replace("^", ""))
                        intent.putExtra(context?.getString(R.string.CATEGORY_ID_EXPENSE), expense.categoryId)
                        intent.putExtra(context?.getString(R.string.EXPENSE_ADDED_BY_TREASURER_UID), expense.addedByTreasurerUid.toString())
                        //intent.putExtra(context?.getString(R.string.))

                        Log.d(HUNTR, "expense.expenseId : " + expense.expenseId)
                        Log.d(HUNTR, "expense.note : " + expense.note)
                        Log.d(HUNTR, "expense.amount : " + expense.amount)
                        Log.d(HUNTR, "expense.category : " + expense.category)
                        Log.d(HUNTR, "expense.dateCreated : " + expense.dateCreated)
                        Log.d(HUNTR, "expense fullName : " + fullName)
                        Log.d(HUNTR, "expense.categoryId : " + expense.categoryId)
                        Log.d(HUNTR, "expense.initial : " + expense.addedByTreasurerInitial)
                        Log.d(HUNTR, "expense addeddByTreasurerUid : " + expense.addedByTreasurerUid)
                        v?.context?.startActivity(intent)
                    }
                })
            }

            view.setOnLongClickListener { v ->
                val expense = expenses[adapterPosition]
                val dialogBuilder = AlertDialog.Builder(view.context)
                dialogBuilder
                    //.setTitle(getString(R.string.confirmation))
                    .setTitle(context?.getString(R.string.confirmation))
                    .setMessage(context?.getString(R.string.delete_expense_message))
                    .setPositiveButton(context?.getString(R.string.yes), { dialog, which ->
                        val dbDeleteExpenseRef = FirebaseDatabase
                            .getInstance()
                            .getReference("expenses")
                            .child(expense.expenseId)
                        dbDeleteExpenseRef.removeValue()
                        showDeletedSuccessfully()
                        notifyItemRemoved(adapterPosition)
                    })
                    .setNegativeButton(context?.getString(R.string.no), { dialog, which ->
                        dialog.dismiss()
                    })

                val dialog = dialogBuilder.create()
                dialog.show()
                true
            }
        }
    }

    private fun showDeletedSuccessfully() {
        Toast.makeText(context, context?.getString(R.string.expense_deleted), Toast.LENGTH_SHORT).show()
    }
}