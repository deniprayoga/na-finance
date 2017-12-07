package com.example.denip.nasyiatulaisyiyahfinance.expense

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.example.denip.nasyiatulaisyiyahfinance.expense.category.PickCategoryExpenseActivity
import com.example.denip.nasyiatulaisyiyahfinance.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_amount_expense.view.*
import kotlinx.android.synthetic.main.activity_expense_detail.*

class ExpenseDetailActivity : AppCompatActivity(), View.OnClickListener,
    CalendarDatePickerDialogFragment.OnDateSetListener {

    companion object {
        private val auth = FirebaseAuth.getInstance()
        private val HUNTR = "huntr_ExpenseDetail"
        private lateinit var glideRequestManager: RequestManager
        private val FRAG_TAG_DATE_PICKER = "fragment_date_picker_name"
        private val dbRef = FirebaseDatabase.getInstance().reference
        private val currentUserUid = auth.currentUser?.uid
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_detail)
        glideRequestManager = Glide.with(this)

        //get intent from ExpenseListAdapter
        val intent = intent
        val expenseId = intent.getStringExtra(getString(R.string.EXPENSE_ID))
        val note = intent.getStringExtra(getString(R.string.EXPENSE_NOTE))
        val amount = intent?.getStringExtra(getString(R.string.EXPENSE_AMOUNT))
        val category = intent.getStringExtra(getString(R.string.EXPENSE_CATEGORY))
        val dateCreated = intent.getStringExtra(getString(R.string.EXPENSE_DATE))
        val categoryId = intent.getStringExtra(getString(R.string.CATEGORY_ID_EXPENSE))
        val fullName = intent.getStringExtra(getString(R.string.EXPENSE_ADDED_BY_TREASURER))
        val addedByTreasurerInitial = intent.getStringExtra(getString(R.string.EXPENSE_ADDED_BY_TREASURER_INITIAL))
        val addedByTreasurerUid = intent.getStringExtra(getString(R.string.EXPENSE_ADDED_BY_TREASURER_UID))

        expense_detail_id_text_view?.text = expenseId
        expense_detail_note_field?.setText(note)
        expense_detail_amount_field.setText(amount)
        expense_detail_categories_field?.setText(category)
        calendar_result_text_expense_detail?.setText(dateCreated)
        expense_detail_category_id_text_view?.text = categoryId
        expense_detail_added_by_treasurer_name?.setText(fullName)

        initLayout()
        initAuth()

        Log.d(HUNTR, "In the onCreate() event")
        Log.d(HUNTR, "expense id : " + expenseId)
        Log.d(HUNTR, "expense note : " + note)
        Log.d(HUNTR, "expense amount : " + amount)
        Log.d(HUNTR, "expense category : " + category)
        Log.d(HUNTR, "expense calendar : " + dateCreated)
        Log.d(HUNTR, "expense category id : " + categoryId)
        Log.d(HUNTR, "fullName : " + fullName)
        Log.d(HUNTR, "expense addedByTreasurerInitial : " + addedByTreasurerInitial)
        Log.d(HUNTR, "expense addedByTreasurerUid : " + addedByTreasurerUid)
        Log.d(HUNTR, "current user Uid : " + currentUserUid)
    }

    private fun initLayout() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        expense_detail_note_field.isCursorVisible = false
        expense_detail_amount_field.isCursorVisible = false
        initToolbar()
        expense_detail_amount_field.isFocusable = false
        expense_detail_categories_field.isFocusable = false
        calendar_result_text_expense_detail.setOnClickListener(this)
        calendar_result_text_expense_detail.isFocusable = false
        expense_detail_amount_field.setOnClickListener(this)
        expense_detail_categories_field.setOnClickListener(this)
        expense_detail_note_field.setOnClickListener(this)
    }

    private fun initAuth() {
        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user: FirebaseUser? = firebaseAuth.currentUser

            if (user == null) {
                finish()
                launchLoginActivity()
            }
        }
        auth.addAuthStateListener(authListener)
    }

    private fun launchLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_expense_detail_layout)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.expense_detail_amount_field -> showAddAmountDialog()
            R.id.calendar_result_text_expense_detail -> showCalendar()
            R.id.expense_detail_categories_field -> pickCategory()
            R.id.expense_detail_note_field -> showCursorOnNoteField()
        }
    }

    private fun showAddAmountDialog() {
        val amount = expense_detail_amount_field.text
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val addAmountInflater: LayoutInflater = layoutInflater
        val view: View = addAmountInflater.inflate(R.layout.activity_add_amount_expense, null)
        view.expense_amount_field_add_amount.text = amount
        view.expense_amount_field_add_amount.movementMethod = null
        view.expense_amount_field_add_amount.isLongClickable = false

        view.one_button?.setOnClickListener {
            val currentAmount = view.expense_amount_field_add_amount?.text.toString()
            view.expense_amount_field_add_amount?.setText(currentAmount.plus(1))
        }

        view.two_button?.setOnClickListener {
            val currentAmount = view.expense_amount_field_add_amount?.text.toString()
            view.expense_amount_field_add_amount?.setText(currentAmount.plus(2))
        }

        view.three_button?.setOnClickListener {
            val currentAmount = view.expense_amount_field_add_amount?.text.toString()
            view.expense_amount_field_add_amount?.setText(currentAmount.plus(3))
        }

        view.four_button?.setOnClickListener {
            val currentAmount = view.expense_amount_field_add_amount?.text.toString()
            view.expense_amount_field_add_amount?.setText(currentAmount.plus(4))
        }

        view.five_button?.setOnClickListener {
            val currentAmount = view.expense_amount_field_add_amount?.text.toString()
            view.expense_amount_field_add_amount?.setText(currentAmount.plus(5))
        }

        view.six_button?.setOnClickListener {
            val currentAmount = view.expense_amount_field_add_amount?.text.toString()
            view.expense_amount_field_add_amount?.setText(currentAmount.plus(6))
        }

        view.seven_button?.setOnClickListener {
            val currentAmount = view.expense_amount_field_add_amount?.text.toString()
            view.expense_amount_field_add_amount?.setText(currentAmount.plus(7))
        }

        view.eight_button?.setOnClickListener {
            val currentAmount = view.expense_amount_field_add_amount?.text.toString()
            view.expense_amount_field_add_amount?.setText(currentAmount.plus(8))
        }

        view.nine_button?.setOnClickListener {
            val currentAmount = view.expense_amount_field_add_amount?.text.toString()
            view.expense_amount_field_add_amount?.setText(currentAmount.plus(9))
        }

        view.zero_button?.setOnClickListener {
            val currentAmount = view.expense_amount_field_add_amount?.text.toString()
            if (currentAmount.isEmpty()) {
                view.expense_amount_field_add_amount?.setText(currentAmount.plus(""))
            } else {
                view.expense_amount_field_add_amount?.setText(currentAmount.plus(0))
            }
        }

        view.clear_button?.setOnClickListener {
            view.expense_amount_field_add_amount?.text?.clear()
        }

        dialogBuilder.setView(view)
        dialogBuilder.setTitle(getString(R.string.amount))
        dialogBuilder.setPositiveButton(getString(R.string.ok), { dialog, _ ->
            run {
                val currentAmount = view.expense_amount_field_add_amount?.text.toString()
                expense_detail_amount_field.text.clear()
                expense_detail_amount_field.setText(currentAmount)
                dialog.dismiss()
                expense_detail_amount_field.error = null
            }
        })
        val dialog: AlertDialog = dialogBuilder.create()
        dialog.show()
        hideCursorOnNoteField()
    }

    private fun pickCategory() {
        startActivity(Intent(this@ExpenseDetailActivity, PickCategoryExpenseActivity::class.java))
    }

    private fun showCalendar() {
        val calendarDatePicker = CalendarDatePickerDialogFragment().setOnDateSetListener(this)
        calendarDatePicker.show(supportFragmentManager, FRAG_TAG_DATE_PICKER)
        hideCursorOnNoteField()
    }

    override fun onDateSet(dialog: CalendarDatePickerDialogFragment?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        calendar_result_text_expense_detail.setText(getString(R.string.calendar_date_picker_result_values, year, monthOfYear + 1, dayOfMonth))
    }

    private fun showCursorOnNoteField() {
        expense_detail_note_field.isCursorVisible = true
    }

    private fun hideCursorOnNoteField() {
        expense_detail_note_field.isCursorVisible = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_expense_detail, menu)
        Log.d(HUNTR, "In the onCreateOptionMenu()")
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        Log.d(HUNTR, "In the onPrepareOptionMenu()")
        val addedByTreasurerUid = intent.getStringExtra(getString(R.string.EXPENSE_ADDED_BY_TREASURER_UID))
        menu?.findItem(R.id.action_bar_save)?.isVisible = addedByTreasurerUid == currentUserUid
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_bar_save -> {
                Log.d(HUNTR, "----------------action bar saved clicked-------------------")
                val amount = expense_detail_amount_field.text.toString().toInt()
                val category = expense_detail_categories_field.text
                val dateCreated = calendar_result_text_expense_detail.text.toString()
                val expenseId = expense_detail_id_text_view.text.toString()
                val note = expense_detail_note_field.text.toString()
                val categoryId = intent.getStringExtra(getString(R.string.CATEGORY_ID_EXPENSE))

                when {
                    amount.toString().isEmpty() -> showWarningAnimation(expense_detail_amount_field)
                    note.isEmpty() -> showWarningAnimation(expense_detail_note_field)

                    else -> {
                        val fullName = intent
                            .getStringExtra(getString(R.string.EXPENSE_ADDED_BY_TREASURER))
                        val addedByTreasurerInitial = intent
                            .getStringExtra(getString(R.string.EXPENSE_ADDED_BY_TREASURER_INITIAL))

                        val expense = ExpenseModel(
                            fullName,
                            currentUserUid,
                            amount,
                            category.toString(),
                            dateCreated,
                            expenseId,
                            note,
                            addedByTreasurerInitial,
                            categoryId)

                        updateExpense(expense)

                        Log.d(HUNTR, "fullName : " + fullName)
                        Log.d(HUNTR, "expense amount : " + amount)
                        Log.d(HUNTR, "expense category : " + category.toString())
                        Log.d(HUNTR, "expense date created : " + dateCreated)
                        Log.d(HUNTR, "expense id : " + expenseId)
                        Log.d(HUNTR, "expense note : " + note)
                        Log.d(HUNTR, "expense category id : " + categoryId)
                    }
                }
            }
        }
        return true
    }

    private fun showWarningAnimation(view: View) {
        val shake = AnimationUtils.loadAnimation(this@ExpenseDetailActivity, R.anim.shake)
        view.startAnimation(shake)
    }

    private fun updateExpense(expense: ExpenseModel?) {
        val dbUpdateExpenseRef = FirebaseDatabase
            .getInstance()
            .getReference("expenses")
            .child(expense?.expenseId)
        val expense = ExpenseModel(
            expense?.addedByTreasurer,
            expense?.addedByTreasurerUid,
            expense?.amount,
            expense?.category,
            expense?.dateCreated,
            expense?.expenseId,
            expense?.note,
            expense?.addedByTreasurerInitial,
            expense?.categoryId)
        dbUpdateExpenseRef.setValue(expense)
        Toast.makeText(applicationContext, getString(R.string.expense_updated), Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(HUNTR, "In the onRestart() event")

        val prefs = PreferenceManager.getDefaultSharedPreferences(this@ExpenseDetailActivity)
        val categoryName = prefs.getString(getString(R.string.CATEGORY_NAME_EXPENSE), "")
        val categoryNumber = prefs.getString(getString(R.string.CATEGORY_NUMBER_EXPENSE), "")
        expense_detail_categories_field?.setText("$categoryNumber $categoryName")
        Log.d(HUNTR + "data", "" + categoryName)
    }

    override fun onResume() {
        super.onResume()
        Log.d(HUNTR, "In the onResume() event")
    }

    override fun onPause() {
        super.onPause()
        Log.d(HUNTR, "In the onPause() event")
    }

    override fun onStop() {
        super.onStop()
        Log.d(HUNTR, "In the onStop() event")
    }

    override fun onStart() {
        super.onStart()
        val addedByTreasurerUid = intent.getStringExtra(getString(R.string.EXPENSE_ADDED_BY_TREASURER_UID))
        expense_detail_amount_field.isEnabled = addedByTreasurerUid == currentUserUid
        expense_detail_note_field.isEnabled = addedByTreasurerUid == currentUserUid
        calendar_result_text_expense_detail.isEnabled = addedByTreasurerUid == currentUserUid
        expense_detail_categories_field.isEnabled = addedByTreasurerUid == currentUserUid
        when (addedByTreasurerUid) {
            currentUserUid -> expense_detail_added_by_treasurer_name.setText(getString(R.string.me))
        }
        Log.d(HUNTR, "In the onStart() event")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(HUNTR, "In the onDestroy() event")
    }
}
