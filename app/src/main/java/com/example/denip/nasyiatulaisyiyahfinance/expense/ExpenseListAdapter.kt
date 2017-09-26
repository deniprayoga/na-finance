package com.example.denip.nasyiatulaisyiyahfinance.expense

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

/**
 * Created by denip on 9/25/2017.
 */

class ExpenseListAdapter(context: Context, expenses: ArrayList<ExpenseModel>) :
    RecyclerView.Adapter<ExpenseListAdapter.CustomViewHolder>() {
    companion object {
        private val auth = FirebaseAuth.getInstance()

    }

    private var context: Context = context
    private var expenses: ArrayList<ExpenseModel> = arrayListOf()

    init {
        this.expenses = expenses
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        val expense = expenses[position]
        holder!!.note.text = expense.note
        holder.category.text = expense.category
        holder.amount.text = expense.amount.toString()
        holder.dateCreated.text = expense.dateCreated
        holder.addedBy.text = expense.addedByTreasure
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

        init {
            view.setOnClickListener { v ->

                val expense = expenses[adapterPosition]
                notifyDataSetChanged()
                val intent = Intent(v?.context, ExpenseDetailActivity::class.java)
                intent.putExtra(context.getString(R.string.EXPENSE_ID), expense.expenseId)
                intent.putExtra(context.getString(R.string.EXPENSE_NOTE), expense.note)
                intent.putExtra(context.getString(R.string.EXPENSE_AMOUNT), expense.amount.toString())
                intent.putExtra(context.getString(R.string.EXPENSE_CATEGORY), expense.category)
                intent.putExtra(context.getString(R.string.EXPENSE_DATE), expense.dateCreated)
                intent.putExtra(context.getString(R.string.EXPENSE_NOTE_PHOTO_URI), expense.notePhotoUri)
                intent.putExtra(context.getString(R.string.EXPENSE_ADDED_BY_TREASURE), auth.currentUser?.email)

                v?.context?.startActivity(intent)
            }

            view.setOnLongClickListener { v ->
                val expense = expenses[adapterPosition]
                val dialogBuilder = AlertDialog.Builder(context)
                dialogBuilder
                    //.setTitle(getString(R.string.confirmation))
                    .setTitle(context.getString(R.string.confirmation))
                    .setMessage(context.getString(R.string.delete_expense_message))
                    .setPositiveButton(context.getString(R.string.yes), { dialog, which ->
                        val dbDeleteExpenseRef = FirebaseDatabase
                            .getInstance()
                            .getReference("expenses")
                            .child(expense.expenseId)
                        dbDeleteExpenseRef.removeValue()
                        showDeletedSuccessfully()
                        notifyItemRemoved(adapterPosition)
                    })
                    .setNegativeButton(context.getString(R.string.no), { dialog, which ->
                        dialog.dismiss()
                    })

                val dialog = dialogBuilder.create()
                dialog.show()
                true
            }
        }
    }

    private fun showDeletedSuccessfully() {
        Toast.makeText(context, context.getString(R.string.expense_deleted), Toast.LENGTH_SHORT).show()
    }
}