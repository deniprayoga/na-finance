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
import kotlinx.android.synthetic.main.activity_add_income.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

class AddIncomeActivity : AppCompatActivity(), View.OnClickListener, CalendarDatePickerDialogFragment.OnDateSetListener {

    companion object {
        val FRAG_TAG_DATE_PICKER = "fragment_date_picker_name"
        var selectedUri: Uri? = null
        lateinit var glideRequestManager: RequestManager
        private val auth = FirebaseAuth.getInstance()
        private val uid = auth.currentUser?.uid
        private val HUNTR = "huntr_AddIncomeAct"
        private val dbAddIncomeRef = FirebaseDatabase.getInstance()?.getReference("incomes")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_income)
        glideRequestManager = Glide.with(this)

        val bundle: Bundle? = intent!!.extras
        val amount: String? = bundle?.getString(getString(R.string.EXTRA_AMOUNT))
        income_amount_field.setText(amount)
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
    }

    private fun initLayout() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        income_amount_field.isFocusable = false
        income_amount_field.isCursorVisible = false
        income_note_field.isCursorVisible = false
        income_categories_field.isFocusable = false
        initToolbar()
        showCurrentDate()
        income_amount_field.setOnClickListener(this)
        income_note_field.setOnClickListener(this)
        income_categories_field.setOnClickListener(this)
        calendar_result_text_income.setOnClickListener(this)
    }

    private fun showCurrentDate() {
        val currentDate = DateTime.now().withZone(DateTimeZone.getDefault()).toString("yyyy-MM-dd")
        calendar_result_text_income.text = currentDate.toString()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_add_income_layout)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.calendar_result_text_income -> showCalendar()
            R.id.income_amount_field -> showAddAmountDialog()
            R.id.income_note_field -> showCursorOnNoteField()
            R.id.income_categories_field -> pickCategory()
        }
    }

    private fun pickCategory() {
        startActivity(Intent(this@AddIncomeActivity, PickCategoryIncomeActivity::class.java))
    }

    private fun showAddAmountDialog() {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val addAmountInflater: LayoutInflater = layoutInflater
        val view: View = addAmountInflater.inflate(R.layout.activity_add_amount_income, null)

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

        dialogBuilder
            .setView(view)
            .setTitle(getString(R.string.amount))
            .setPositiveButton(getString(R.string.ok), { dialog, which ->
                run {
                    val currentAmount = view.income_amount_field_add_amount?.text.toString()
                    income_amount_field.text.clear()
                    income_amount_field.setText(currentAmount)
                    dialog.dismiss()
                    income_amount_field.error = null
                }
            })
            .create().show()
    }

    private fun hideCursorOnNoteField() {
        income_note_field.isCursorVisible = false
    }

    private fun showCursorOnNoteField() {
        income_note_field?.isCursorVisible = true
    }

    private fun showCalendar() {
        val calendarDatePicker = CalendarDatePickerDialogFragment().setOnDateSetListener(this)
        calendarDatePicker.show(supportFragmentManager, FRAG_TAG_DATE_PICKER)
    }

    override fun onDateSet(dialog: CalendarDatePickerDialogFragment?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        calendar_result_text_income.text = getString(R.string.calendar_date_picker_result_values, year, monthOfYear + 1, dayOfMonth)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_income, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_bar_done -> saveIncome()
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    private fun saveIncome() {
        val dateCreated = calendar_result_text_income?.text.toString()
        val amount = income_amount_field?.text.toString()
        val note = income_note_field?.text.toString()
        //val notePhotoUri = selectedUri.toString()
        val category = income_categories_field?.text.toString()

        when {
            amount.isEmpty() -> showWarningAnimation(income_amount_field)
            note.isEmpty() -> showWarningAnimation(income_note_field)
            category.isEmpty() -> showWarningAnimation(income_categories_field)
            else -> {
                val dbRef = FirebaseDatabase.getInstance().getReference("users")

                val prefs = PreferenceManager.getDefaultSharedPreferences(this@AddIncomeActivity)
                val categoryId = prefs.getString(getString(R.string.CATEGORY_ID_INCOME), "")
                dbRef.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(databaseError: DatabaseError?) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        val incomeId: String? = dbAddIncomeRef?.push()?.key
                        val fullName = dataSnapshot?.child(uid)?.child("fullName")
                            ?.value.toString()
                        val initial = dataSnapshot?.child(uid)?.child("initial")?.value.toString()

                        val income = IncomeModel(
                            incomeId,
                            fullName,
                            amount.toInt(),
                            category,
                            dateCreated,
                            note,
                            initial,
                            uid,
                            categoryId)
                        dbAddIncomeRef?.child(incomeId)?.setValue(income)

                        Log.d(HUNTR, "income amount : " + income.amount)
                        Log.d(HUNTR, "income note : " + income.note)
                        Log.d(HUNTR, "income added by treasurer uid : " + income.addedByTreasurerUid)
                        Log.d(HUNTR, "income added by treasurer initial : " + income.addedByTreasurerInitial)
                        Log.d(HUNTR, "income added by treasurer : " + income.addedByTreasurer)
                        Log.d(HUNTR, "income category : " + income.category)
                        Log.d(HUNTR, "income category id : " + income.categoryId)
                        Log.d(HUNTR, "income date created : " + income.dateCreated)
                        Log.d(HUNTR, "income id : " + income.incomeId)

                        Toast.makeText(this@AddIncomeActivity, getString(R.string.income_added),
                            Toast.LENGTH_SHORT).show()
                        finish()
                    }
                })


            }
        }
    }

    private fun showWarningAnimation(view: View) {
        val shake = AnimationUtils.loadAnimation(this@AddIncomeActivity, R.anim.shake)
        view.startAnimation(shake)
    }

    override fun onRestart() {
        super.onRestart()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this@AddIncomeActivity)
        val categoryNumber = prefs.getString(getString(R.string.CATEGORY_NUMBER_INCOME), "")
        val categoryName = prefs.getString(getString(R.string.CATEGORY_NAME_INCOME), "")
        income_categories_field?.error = null
        income_categories_field?.setText("$categoryNumber $categoryName")
    }
}
