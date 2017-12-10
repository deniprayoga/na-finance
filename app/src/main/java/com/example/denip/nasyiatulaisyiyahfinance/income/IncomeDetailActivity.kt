package com.example.denip.nasyiatulaisyiyahfinance.income

import android.content.Intent
import android.net.Uri
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
import com.example.denip.nasyiatulaisyiyahfinance.income.category.PickCategoryIncomeActivity
import com.example.denip.nasyiatulaisyiyahfinance.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_amount_income.view.*
import kotlinx.android.synthetic.main.activity_income_detail.*

class IncomeDetailActivity : AppCompatActivity(), View.OnClickListener,
    CalendarDatePickerDialogFragment.OnDateSetListener {

    companion object {
        private val auth = FirebaseAuth.getInstance()
        private val HUNTR = "huntr_IncomeDetail"
        private var selectedUri: Uri? = null
        private lateinit var glideRequestManager: RequestManager
        private val currentUserUid = auth.currentUser?.uid
        private val FRAG_TAG_DATE_PICKER = "fragment_date_picker_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income_detail)
        glideRequestManager = Glide.with(this)

        //get intent from IncomeListAdapter
        val intent = intent
        val incomeId = intent.getStringExtra(getString(R.string.INCOME_ID))
        val note = intent.getStringExtra(getString(R.string.INCOME_NOTE))
        val amount = intent?.getStringExtra(getString(R.string.INCOME_AMOUNT))
        val category = intent.getStringExtra(getString(R.string.INCOME_CATEGORY))
        val dateCreated = intent.getStringExtra(getString(R.string.INCOME_DATE))
        val categoryId = intent.getStringExtra(getString(R.string.CATEGORY_ID_INCOME))
        val fullName = intent.getStringExtra(getString(R.string.INCOME_ADDED_BY_TREASURER))
        val addedByTreasurerInitial = intent.getStringExtra(getString(R.string.INCOME_ADDED_BY_TREASURER_INITIAL))
        val addedByTreasurerUid = intent.getStringExtra(getString(R.string.INCOME_ADDED_BY_TREASURER_UID))

        income_detail_id_text_view?.text = incomeId
        income_detail_note_field?.setText(note)
        income_detail_amount_field.setText(amount)
        income_detail_categories_field?.setText(category)
        calendar_result_text_income_detail?.setText(dateCreated)
        income_detail_category_id_text_view?.text = categoryId
        income_detail_added_by_treasurer_name?.setText(fullName)

        Log.d(HUNTR, "In the onCreate() event")
        Log.d(HUNTR, "income id : " + incomeId)
        Log.d(HUNTR, "income note : " + note)
        Log.d(HUNTR, "income amount : " + amount)
        Log.d(HUNTR, "income category : " + category)
        Log.d(HUNTR, "income calendar : " + dateCreated)
        Log.d(HUNTR, "income category id : " + categoryId)
        Log.d(HUNTR, "fullName : " + fullName)
        Log.d(HUNTR, "income addedByTreasurerInitial : " + addedByTreasurerInitial)
        Log.d(HUNTR, "income addedByTreasurerUid : " + addedByTreasurerUid)
        Log.d(HUNTR, "income user Uid : " + currentUserUid)

        initLayout()
        initAuth()
    }

    private fun initAuth() {
        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user: FirebaseUser? = firebaseAuth.currentUser

            if (user == null) {
                auth.signOut()
                launchLoginActivity()
                finish()
            }
        }
        auth.addAuthStateListener(authListener)
    }

    private fun launchLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun initLayout() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        income_detail_note_field.isCursorVisible = false
        income_detail_amount_field.isCursorVisible = false
        income_detail_amount_field.isFocusable = false
        income_detail_categories_field.isFocusable = false
        initToolbar()
        calendar_result_text_income_detail.setOnClickListener(this)
        calendar_result_text_income_detail.isFocusable = false
        income_detail_amount_field.setOnClickListener(this)
        income_detail_categories_field.setOnClickListener(this)
        income_detail_note_field.setOnClickListener(this)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_income_detail_layout)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_income_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_bar_save -> updateIncome()
        }

        return true
    }

    private fun updateIncome() {
        val amount = income_detail_amount_field.text.toString().toInt()
        val category = income_detail_categories_field.text.toString()
        val dateCreated = calendar_result_text_income_detail.text.toString()
        val incomeId = income_detail_id_text_view.text.toString()
        val note = income_detail_note_field.text.toString()
        val categoryId = intent.getStringExtra(getString(R.string.CATEGORY_ID_INCOME))

        when {
            amount.toString().isEmpty() -> showWarningAnimation(income_detail_amount_field)
            note.isEmpty() -> showWarningAnimation(income_detail_note_field)
            else -> {
                val dbRef = FirebaseDatabase.getInstance().getReference("users")
                dbRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        val fullName = dataSnapshot?.child(auth.currentUser?.uid)?.child("fullName")
                            ?.value.toString()

                        val addedByTreasurerInitial = intent
                            .getStringExtra(getString(R.string.INCOME_ADDED_BY_TREASURER_INITIAL))
                        val dbUpdateIncomeRef = FirebaseDatabase
                            .getInstance()
                            .getReference("incomes")
                            .child(incomeId)
                        val income = IncomeModel(
                            incomeId,
                            fullName,
                            amount,
                            category,
                            dateCreated,
                            note,
                            addedByTreasurerInitial,
                            currentUserUid,
                            categoryId)
                        dbUpdateIncomeRef.setValue(income)
                        Toast.makeText(applicationContext, getString(R.string.income_updated), Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }

                    override fun onCancelled(databaseError: DatabaseError?) {

                    }
                })
            }
        }
    }

    private fun showWarningAnimation(view: View) {
        val shake = AnimationUtils.loadAnimation(this@IncomeDetailActivity, R.anim.shake)
        view.startAnimation(shake)
    }

    override fun onDateSet(dialog: CalendarDatePickerDialogFragment?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        calendar_result_text_income_detail.setText(getString(R.string.calendar_date_picker_result_values, year, monthOfYear + 1, dayOfMonth))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.income_detail_amount_field -> showAddAmountDialog()
            R.id.calendar_result_text_income_detail -> showCalendar()
            R.id.income_detail_categories_field -> pickCategory()
            R.id.income_detail_note_field -> showCursorOnNoteField()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val addedByTreasurerUid = intent.getStringExtra(getString(R.string.INCOME_ADDED_BY_TREASURER_UID))
        menu?.findItem(R.id.action_bar_save)?.isVisible = addedByTreasurerUid == currentUserUid
        return super.onPrepareOptionsMenu(menu)
    }

    private fun pickCategory() {
        startActivity(Intent(this@IncomeDetailActivity, PickCategoryIncomeActivity::class.java))
    }

    private fun showAddAmountDialog() {
        val amount = income_detail_amount_field.text
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val addAmountInflater: LayoutInflater = layoutInflater
        val view: View = addAmountInflater.inflate(R.layout.activity_add_amount_income, null)
        view.income_amount_field_add_amount.text = amount

        view.one_button?.setOnClickListener {
            val currentAmount = view.income_amount_field_add_amount?.text.toString()
            view.income_amount_field_add_amount?.setText(currentAmount.plus(1))
        }

        view.two_button?.setOnClickListener {
            val currentAmount = view.income_amount_field_add_amount?.text.toString()
            view.income_amount_field_add_amount?.setText(currentAmount.plus(2))
        }

        view.three_button?.setOnClickListener {
            val currentAmount = view.income_amount_field_add_amount?.text.toString()
            view.income_amount_field_add_amount?.setText(currentAmount.plus(3))
        }

        view.four_button?.setOnClickListener {
            val currentAmount = view.income_amount_field_add_amount?.text.toString()
            view.income_amount_field_add_amount?.setText(currentAmount.plus(4))
        }

        view.five_button?.setOnClickListener {
            val currentAmount = view.income_amount_field_add_amount?.text.toString()
            view.income_amount_field_add_amount?.setText(currentAmount.plus(5))
        }

        view.six_button?.setOnClickListener {
            val currentAmount = view.income_amount_field_add_amount?.text.toString()
            view.income_amount_field_add_amount?.setText(currentAmount.plus(6))
        }

        view.seven_button?.setOnClickListener {
            val currentAmount = view.income_amount_field_add_amount?.text.toString()
            view.income_amount_field_add_amount?.setText(currentAmount.plus(7))
        }

        view.eight_button?.setOnClickListener {
            val currentAmount = view.income_amount_field_add_amount?.text.toString()
            view.income_amount_field_add_amount?.setText(currentAmount.plus(8))
        }

        view.nine_button?.setOnClickListener {
            val currentAmount = view.income_amount_field_add_amount?.text.toString()
            view.income_amount_field_add_amount?.setText(currentAmount.plus(9))
        }

        view.zero_button?.setOnClickListener {
            val currentAmount = view.income_amount_field_add_amount?.text.toString()
            if (currentAmount.isEmpty()) {
                view.income_amount_field_add_amount?.setText(currentAmount.plus(""))
            } else {
                view.income_amount_field_add_amount?.setText(currentAmount.plus(0))
            }
        }

        view.clear_button?.setOnClickListener {
            view.income_amount_field_add_amount?.text?.clear()
        }

        dialogBuilder.setView(view)
        dialogBuilder.setTitle(getString(R.string.amount))
        dialogBuilder.setPositiveButton(getString(R.string.ok), { dialog, _ ->
            run {
                val currentAmount = view.income_amount_field_add_amount?.text.toString()
                income_detail_amount_field.text.clear()
                income_detail_amount_field.setText(currentAmount)
                dialog.dismiss()
                income_detail_amount_field.error = null
            }
        })
        val dialog: AlertDialog = dialogBuilder.create()
        dialog.show()
        hideCursorOnNoteField()
    }

    private fun showCalendar() {
        val calendarDatePicker = CalendarDatePickerDialogFragment().setOnDateSetListener(this)
        calendarDatePicker.show(supportFragmentManager, FRAG_TAG_DATE_PICKER)
        hideCursorOnNoteField()
    }

    private fun showCursorOnNoteField() {
        income_detail_note_field.isCursorVisible = true
    }

    private fun hideCursorOnNoteField() {
        income_detail_note_field.isCursorVisible = false
    }

    override fun onStart() {
        super.onStart()
        val addedByTreasurerUid = intent.getStringExtra(getString(R.string.INCOME_ADDED_BY_TREASURER_UID))
        income_detail_amount_field.isEnabled = addedByTreasurerUid == currentUserUid
        income_detail_note_field.isEnabled = addedByTreasurerUid == currentUserUid
        calendar_result_text_income_detail.isEnabled = addedByTreasurerUid == currentUserUid
        income_detail_categories_field.isEnabled = addedByTreasurerUid == currentUserUid
        when (addedByTreasurerUid) {
            currentUserUid -> income_detail_added_by_treasurer_name.setText(getString(R.string.me))
        }
        Log.d(HUNTR, "In the onStart() event")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(HUNTR, "In the onRestart() event")
        val prefsIncome = PreferenceManager.getDefaultSharedPreferences(this@IncomeDetailActivity)
        val categoryName = prefsIncome.getString(getString(R.string.CATEGORY_NAME_INCOME), "")
        val categoryNumber = prefsIncome.getString(getString(R.string.CATEGORY_NUMBER_INCOME), "")
        income_detail_categories_field?.setText("$categoryNumber $categoryName")
        Log.d(HUNTR + "data", "" + categoryName)
        Log.d(HUNTR + "text", "" + income_detail_categories_field?.text.toString())
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

    override fun onDestroy() {
        super.onDestroy()
        Log.d(HUNTR, "In the onDestroy() event")
    }
}
