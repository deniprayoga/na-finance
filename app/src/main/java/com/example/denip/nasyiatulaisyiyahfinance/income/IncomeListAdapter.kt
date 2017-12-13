package com.example.denip.nasyiatulaisyiyahfinance.income

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
import java.text.NumberFormat
import java.util.*

/**
 * Created by denip on 9/25/2017.
 */

class IncomeListAdapter(context: Context?, incomes: ArrayList<IncomeModel>) : RecyclerView.Adapter<IncomeListAdapter.CustomViewHolder>() {

    companion object {
        private val auth = FirebaseAuth.getInstance()
        private val HUNTR = "huntr_IncomeListAdptr"
        private val dbRefUsers = FirebaseDatabase.getInstance().getReference("users")
        private val currentUserUid = auth.currentUser?.uid
    }

    private var context: Context? = context
    private var incomes: ArrayList<IncomeModel> = arrayListOf()

    init {
        this.incomes = incomes
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        val income = incomes[position]
        dbRefUsers.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError?) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                val formattedAmount = formatAmount(income.amount!!)
                holder!!.note.text = income.note.toString()
                holder.category.text = income.category.toString()
                holder.amount.text = "Rp $formattedAmount"
                holder.dateCreated.text = income.dateCreated.toString()
                holder.addedBy.text = dataSnapshot?.child(income.addedByTreasurerUid)
                    ?.child("fullName")?.value.toString()
                holder.categoryId.text = income.categoryId.toString()
                holder.addedByInitial.text = dataSnapshot?.child(income.addedByTreasurerUid)
                    ?.child("initial")?.value.toString()

            }
        })
    }

    private fun formatAmount(amount: Long): String =
        NumberFormat.getNumberInstance(Locale.US).format(amount)

    override fun getItemCount(): Int = incomes.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.income_list_row_view, parent, false)
        return CustomViewHolder(view)
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val addedBy = view.findViewById(R.id.income_list_row_view_added_by) as TextView
        val amount = view.findViewById(R.id.income_list_row_view_amount) as TextView
        val category = view.findViewById(R.id.income_list_row_view_category) as TextView
        val dateCreated = view.findViewById(R.id.income_list_row_view_date) as TextView
        val note = view.findViewById(R.id.income_list_row_view_note) as TextView
        val categoryId = view.findViewById(R.id.income_list_row_view_category_id) as TextView
        val addedByInitial = view.findViewById(R.id.initial_field) as TextView

        init {
            view.setOnClickListener { v ->
                Log.d(HUNTR, "-----------------in setOnClickListener()------------------")
                dbRefUsers.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(databaseError: DatabaseError?) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {

                        val income = incomes[adapterPosition]
                        val fullName = dataSnapshot?.child(income.addedByTreasurerUid)?.child("fullName")?.value.toString()
                        val intent = Intent(v?.context, IncomeDetailActivity::class.java)
                        intent.putExtra(context?.getString(R.string.INCOME_ID), income.incomeId)
                        intent.putExtra(context?.getString(R.string.INCOME_NOTE), income.note.toString())
                        intent.putExtra(context?.getString(R.string.INCOME_AMOUNT), income.amount.toString())
                        intent.putExtra(context?.getString(R.string.INCOME_CATEGORY), income.category.toString())
                        intent.putExtra(context?.getString(R.string.INCOME_DATE), income.dateCreated.toString())
                        intent.putExtra(context?.getString(R.string.INCOME_NOTE_PHOTO_URI), income.addedByTreasurerInitial)
                        intent.putExtra(context?.getString(R.string.INCOME_ADDED_BY_TREASURER), fullName)
                        intent.putExtra(context?.getString(R.string.CATEGORY_ID_INCOME), income.categoryId)
                        intent.putExtra(context?.getString(R.string.INCOME_ADDED_BY_TREASURER_UID), income.addedByTreasurerUid.toString())

                        Log.d(HUNTR, "income.expenseId : " + income.incomeId)
                        Log.d(HUNTR, "income.note : " + income.note)
                        Log.d(HUNTR, "income.amount : " + income.amount)
                        Log.d(HUNTR, "income.category : " + income.category)
                        Log.d(HUNTR, "income.dateCreated : " + income.dateCreated)
                        Log.d(HUNTR, "income fullName : " + fullName)
                        Log.d(HUNTR, "income.categoryId : " + income.categoryId)
                        Log.d(HUNTR, "income.initial : " + income.addedByTreasurerInitial)
                        Log.d(HUNTR, "income addeddByTreasurerUid : " + income.addedByTreasurerUid)

                        v?.context?.startActivity(intent)
                    }
                })
            }

            view.setOnLongClickListener { v ->
                dbRefUsers.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(databaseError: DatabaseError?) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        val income = incomes[adapterPosition]

                        when (currentUserUid) {
                            income.addedByTreasurerUid -> showDeleteDialog(view, adapterPosition)
                            else -> {
                            }
                        }
                    }
                })
                true
            }
        }
    }

    private fun showDeleteDialog(view: View, adapterPosition: Int) {
        val income = incomes[adapterPosition]
        val dialogBuilder = AlertDialog.Builder(view.context)
        dialogBuilder
            //.setTitle(getString(R.string.confirmation))
            .setTitle(context?.getString(R.string.confirmation))
            .setMessage(context?.getString(R.string.delete_income_message))
            .setPositiveButton(context?.getString(R.string.yes), { dialog, which ->
                val dbDeleteExpenseRef = FirebaseDatabase
                    .getInstance()
                    .getReference("incomes")
                    .child(income.incomeId)
                dbDeleteExpenseRef.removeValue()
                showDeletedSuccessfully()
                dialog.dismiss()
                notifyItemRemoved(adapterPosition)
            })
            .setNegativeButton(context?.getString(R.string.no), { dialog, which ->
                dialog.dismiss()
            })

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun showDeletedSuccessfully() {
        Toast.makeText(context, context?.getString(R.string.income_deleted), Toast.LENGTH_SHORT).show()
    }
}