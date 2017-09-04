package com.example.denip.nasyiatulaisyiyahfinance

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_add_amount.*

class AddAmountActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_amount)

        initLayout()

        val bundle: Bundle? = intent!!.extras
        val amount: String? = bundle?.getString(getString(R.string.EXTRA_AMOUNT))
        expense_amount_field_add_amount.setText(amount)
    }

    private fun initLayout() {
        expense_amount_field_add_amount?.isFocusable = false
        initToolbar()
        initNumpad()
    }

    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar_add_amount_layout) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initNumpad() {
        one_button.setOnClickListener(this)
        two_button.setOnClickListener(this)
        three_button.setOnClickListener(this)
        four_button.setOnClickListener(this)
        five_button.setOnClickListener(this)
        six_button.setOnClickListener(this)
        seven_button.setOnClickListener(this)
        eight_button.setOnClickListener(this)
        nine_button.setOnClickListener(this)
        zero_button.setOnClickListener(this)
        clear_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.zero_button -> {
                addZero()
            }
            R.id.one_button -> {
                addNumberToField(1)
            }
            R.id.two_button -> {
                addNumberToField(2)
            }
            R.id.three_button -> {
                addNumberToField(3)
            }
            R.id.four_button -> {
                addNumberToField(4)
            }
            R.id.five_button -> {
                addNumberToField(5)
            }
            R.id.six_button -> {
                addNumberToField(6)
            }
            R.id.seven_button -> {
                addNumberToField(7)
            }
            R.id.eight_button -> {
                addNumberToField(8)
            }
            R.id.nine_button -> {
                addNumberToField(9)
            }
            R.id.clear_button -> {
                clearField()
            }
        }
    }

    private fun addNumberToField(num: Int?) {
        val currentAmount = expense_amount_field_add_amount?.text.toString()
        expense_amount_field_add_amount?.setText(currentAmount.plus(num))
    }

    private fun addZero() {
        val currentAmount = expense_amount_field_add_amount?.text.toString()
        if (currentAmount.isEmpty()) {
            addEmptyString("")
        } else {
            addNumberToField(0)
        }
    }

    private fun clearField() {
        expense_amount_field_add_amount.text.clear()
    }

    private fun addEmptyString(s: String?) {
        val currentAmount = expense_amount_field_add_amount?.text.toString()
        expense_amount_field_add_amount?.setText(currentAmount.plus(s))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_bar_done -> finish()
            android.R.id.home -> {
                val currentAmount = expense_amount_field_add_amount?.text.toString()
                val intent = Intent(this, AddExpenseActivity::class.java)
                intent.putExtra(getString(R.string.EXTRA_AMOUNT), currentAmount)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
        return true
    }
}
