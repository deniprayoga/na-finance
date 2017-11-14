package com.example.denip.nasyiatulaisyiyahfinance.income

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

/**
 * Created by denip on 9/25/2017.
 */

class IncomeListAdapter(context: Context?, incomes: ArrayList<IncomeModel>) : RecyclerView.Adapter<IncomeListAdapter.CustomViewHolder>() {

    companion object {
        private val auth = FirebaseAuth.getInstance()
    }

    private var context: Context? = context
    private var incomes: ArrayList<IncomeModel> = arrayListOf()

    init {
        this.incomes = incomes
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        val income = incomes[position]
        holder!!.note.text = income.note
        holder.category.text = income.category
        holder.amount.text = "Rp. ${income.amount.toString()}"
        holder.dateCreated.text = income.dateCreated
        holder.addedBy.text = income.addedByTreasure

    }

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

        init {
            view.setOnClickListener { v ->
                val income = incomes[adapterPosition]
                val intent = Intent(v?.context, IncomeDetailActivity::class.java)
                intent.putExtra(context?.getString(R.string.INCOME_ID), income.incomeId)
                intent.putExtra(context?.getString(R.string.INCOME_NOTE), income.note)
                intent.putExtra(context?.getString(R.string.INCOME_AMOUNT), income.amount.toString())
                intent.putExtra(context?.getString(R.string.INCOME_CATEGORY), income.category)
                intent.putExtra(context?.getString(R.string.INCOME_DATE), income.dateCreated)
                intent.putExtra(context?.getString(R.string.INCOME_NOTE_PHOTO_URI), income.notePhotoUri)
                intent.putExtra(context?.getString(R.string.INCOME_ADDED_BY_TREASURE), income.addedByTreasure)

                v?.context?.startActivity(intent)
            }

            view.setOnLongClickListener { v ->
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
                true
            }
        }
    }

    private fun showDeletedSuccessfully() {
        Toast.makeText(context, context?.getString(R.string.income_deleted), Toast.LENGTH_SHORT).show()
    }
}