package com.example.denip.nasyiatulaisyiyahfinance

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_expense.*

class AddExpenseActivity : AppCompatActivity(), View.OnClickListener {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        expense_value_field.text

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
        delete_field_button.setOnClickListener(this)
        check_value_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.check_value_button -> {
                val expenseValueField = expense_value_field?.text.toString()
                Toast.makeText(this, expenseValueField, Toast.LENGTH_LONG).show()
            }

            R.id.zero_button -> {
                addNumberToField(0)
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
            R.id.delete_field_button -> {
                //removeLastNumber()
                val number = expense_value_field.text.toString()
                val numbernew = number.substring(0, number.length - 1)
                expense_value_field.setText(numbernew)
            }
        }
    }

    fun removeLastNumber(): String {
        val number = expense_value_field.text.toString()
        if (number.isNotEmpty()) {
            number.substring(0, number.length - 1)
            expense_value_field.setText(number)
        }
        return number
    }

    fun addNumberToField(num: Int?) {
        val expenseValueField = expense_value_field?.text.toString()
        expense_value_field?.setText(expenseValueField?.plus(num))
    }
}