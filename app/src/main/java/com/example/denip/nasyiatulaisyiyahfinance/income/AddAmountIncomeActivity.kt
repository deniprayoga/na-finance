package com.example.denip.nasyiatulaisyiyahfinance.income

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.example.denip.nasyiatulaisyiyahfinance.R
import kotlinx.android.synthetic.main.activity_add_amount_income.*

class AddAmountIncomeActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_amount_income)

        initLayout()
    }

    private fun initLayout() {
        income_amount_field_add_amount?.isFocusable = false
        initNumpad()
        initAmount()
    }

    private fun initAmount() {
        val bundle: Bundle? = intent!!.extras
        val amount: String? = bundle?.getString(getString(R.string.EXTRA_AMOUNT))
        income_amount_field_add_amount.setText(amount)
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

    private fun clearField() {
        income_amount_field_add_amount.text.clear()
    }

    private fun addNumberToField(num: Int) {
        val currentAmount = income_amount_field_add_amount.text.toString()
        income_amount_field_add_amount.setText(currentAmount.plus(num))
    }

    private fun addZero() {
        val currentAmount = income_amount_field_add_amount.text.toString()
        if (currentAmount.isEmpty()) {
            addEmptyString("")
        } else {
            addNumberToField(0)
        }
    }

    private fun addEmptyString(s: String) {
        val currentAmount = income_amount_field_add_amount.text.toString()
        income_amount_field_add_amount.setText(currentAmount.plus(s))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                val currentAmount = income_amount_field_add_amount.text.toString()
                val intent = Intent(this, AddIncomeActivity::class.java)
                intent.putExtra(getString(R.string.EXTRA_AMOUNT), currentAmount)
                intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
                startActivity(intent)
            }
        }
        return true
    }
}
