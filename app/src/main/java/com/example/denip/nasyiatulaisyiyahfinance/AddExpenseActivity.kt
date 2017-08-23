package com.example.denip.nasyiatulaisyiyahfinance

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment
import kotlinx.android.synthetic.main.activity_add_expense.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

class AddExpenseActivity : AppCompatActivity(), View.OnClickListener,
    CalendarDatePickerDialogFragment.OnDateSetListener {

    companion object {
        val FRAG_TAG_DATE_PICKER = "fragment_date_picker_name"
        val context: Context? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        expense_value_field.text

        showCurrentDate()

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
        check_value_button.setOnClickListener(this)
        calendar_button.setOnClickListener(this)

    }

    override fun onDateSet(dialog: CalendarDatePickerDialogFragment?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        calendar_result_text.text = getString(R.string.calendar_date_picker_result_values, year, monthOfYear, dayOfMonth)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.check_value_button -> {
                val expenseValueField = expense_value_field.text.toString()
                Toast.makeText(this, expenseValueField, Toast.LENGTH_SHORT).show()
            }
            R.id.zero_button -> {
                val currentValue = expense_value_field.text.toString()
                if (currentValue.isEmpty()) {
                    avoidZeroInFirstInput("")
                } else {
                    addNumberToField(0)
                }
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
            R.id.calendar_button -> {
                showCalendar()
            }
        }
    }

    private fun showCalendar() {
        val calendarDatePicker = CalendarDatePickerDialogFragment()
            .setOnDateSetListener(this)
        calendarDatePicker.show(supportFragmentManager, FRAG_TAG_DATE_PICKER)
    }

    private fun showCurrentDate() {
        val currentDate = DateTime.now().withZone(DateTimeZone.getDefault()).toString("dd-MM-yyyy")
        calendar_result_text.text = currentDate.toString()
    }

    private fun clearField() {
        expense_value_field.text.clear()
    }

    private fun addNumberToField(num: Int?) {
        val expenseValueField = expense_value_field?.text.toString()
        expense_value_field?.setText(expenseValueField.plus(num))
    }

    private fun avoidZeroInFirstInput(s: String?) {
        val expenseValueField = expense_value_field?.text.toString()
        expense_value_field?.setText(expenseValueField.plus(s))
    }
}