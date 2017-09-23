package com.example.denip.nasyiatulaisyiyahfinance.expense

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.denip.nasyiatulaisyiyahfinance.R

/**
 * Created by denip on 9/21/2017.
 */

class ExpenseList(context: Activity, expenses: List<ExpenseModel>) : ArrayAdapter<ExpenseModel>(context, R.layout.layout_expense_list, expenses) {

    private var context: Activity? = null
    private var expenses: List<ExpenseModel> = listOf()

    init {
        this.context = context
        this.expenses = expenses
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context!!.layoutInflater
        val view = inflater!!.inflate(R.layout.layout_expense_list, null, true)

        val addedBy = view.findViewById(R.id.added_by_text_view) as TextView
        val amount = view.findViewById(R.id.amount_text_view) as TextView
        val category = view.findViewById(R.id.category_text_view) as TextView
        val dateCreated = view.findViewById(R.id.date_created_text_view) as TextView
        val note = view.findViewById(R.id.note_text_view) as TextView

        val expense = expenses[position]
        addedBy.text = expense.addedByTreasure
        amount.text = expense.amount.toString()
        category.text = expense.category
        dateCreated.text = expense.dateCreated
        note.text = expense.note

        return view
    }
}