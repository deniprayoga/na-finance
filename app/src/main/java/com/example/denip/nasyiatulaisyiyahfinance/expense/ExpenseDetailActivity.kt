package com.example.denip.nasyiatulaisyiyahfinance.expense

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.example.denip.nasyiatulaisyiyahfinance.R
import kotlinx.android.synthetic.main.activity_expense_detail.*

class ExpenseDetailActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_detail)

        val intent = intent
        val expenseId = intent.getStringExtra(getString(R.string.EXPENSE_ID))
        val note = intent.getStringExtra(getString(R.string.EXPENSE_NOTE))
        val amount = intent?.getStringExtra(getString(R.string.EXPENSE_AMOUNT))
        val category = intent.getStringExtra(getString(R.string.EXPENSE_CATEGORY))
        val dateCreated = intent.getStringExtra(getString(R.string.EXPENSE_DATE))

        expense_detail_id_text_view?.text = expenseId
        expense_detail_note_field?.setText(note)
        expense_detail_amount_field.setText(amount)
        expense_detail_categories_field?.setText(category)
        calendar_result_text_expense_detail?.text = dateCreated
        initLayout()

        Log.d("amouuu", amount.toString())
    }

    private fun initLayout() {
        initToolbar()
        expense_detail_amount_field.isFocusable = false
        expense_detail_categories_field.isFocusable = false
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_expense_detail_layout)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
