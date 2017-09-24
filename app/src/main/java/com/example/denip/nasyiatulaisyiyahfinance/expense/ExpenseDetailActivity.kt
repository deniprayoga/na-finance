package com.example.denip.nasyiatulaisyiyahfinance.expense

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.example.denip.nasyiatulaisyiyahfinance.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedbottompicker.TedBottomPicker
import kotlinx.android.synthetic.main.activity_add_amount_expense.view.*
import kotlinx.android.synthetic.main.activity_expense_detail.*

class ExpenseDetailActivity : AppCompatActivity(), View.OnClickListener,
    CalendarDatePickerDialogFragment.OnDateSetListener {

    companion object {
        private val auth = FirebaseAuth.getInstance()
        private val TAGGG = "huisjkf_ExpenseDetail"
        private var selectedUri: Uri? = null
        private lateinit var glideRequestManager: RequestManager
        private val FRAG_TAG_DATE_PICKER = "fragment_date_picker_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_detail)
        glideRequestManager = Glide.with(this)

        val intent = intent
        val expenseId = intent.getStringExtra(getString(R.string.EXPENSE_ID))
        val note = intent.getStringExtra(getString(R.string.EXPENSE_NOTE))
        val amount = intent?.getStringExtra(getString(R.string.EXPENSE_AMOUNT))
        val category = intent.getStringExtra(getString(R.string.EXPENSE_CATEGORY))
        val dateCreated = intent.getStringExtra(getString(R.string.EXPENSE_DATE))
        val addedByTreasure = intent.getStringExtra(getString(R.string.EXPENSE_ADDED_BY_TREASURE))

        expense_detail_id_text_view?.text = expenseId
        expense_detail_note_field?.setText(note)
        expense_detail_amount_field.setText(amount)
        expense_detail_categories_field?.setText(category)
        calendar_result_text_expense_detail?.text = dateCreated
        initLayout()
        initAuth()

        Log.d("amouuu", amount.toString())
        Log.d(TAGGG, "In the onCreate() event")
    }

    private fun initLayout() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        expense_detail_note_field.isCursorVisible = false
        expense_detail_amount_field.isCursorVisible = false
        initToolbar()
        expense_detail_amount_field.isFocusable = false
        expense_detail_categories_field.isFocusable = false
        calendar_button_expense_detail.setOnClickListener(this)
        calendar_result_text_expense_detail.setOnClickListener(this)
        pick_image_button_expense_detail.setOnClickListener(this)
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
            R.id.calendar_button_expense_detail -> showCalendar()
            R.id.pick_image_button_expense_detail -> pickImage()
            R.id.expense_detail_amount_field -> showAddAmountDialog()
            R.id.calendar_result_text_expense_detail -> showCalendar()
            R.id.expense_detail_categories_field -> showCategoryDialog()
            R.id.expense_detail_note_field -> showCursorOnNoteField()
        }
    }

    private fun showAddAmountDialog() {
        val amount = expense_detail_amount_field.text
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val addAmountInflater: LayoutInflater = layoutInflater
        val view: View = addAmountInflater.inflate(R.layout.activity_add_amount_expense, null)
        view.expense_amount_field_add_amount.text = amount

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

    private fun showCategoryDialog() {
        hideCursorOnNoteField()
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val categoryInflater: LayoutInflater = layoutInflater
        val view: View = categoryInflater.inflate(R.layout.pick_category_dialog, null)
        dialogBuilder.setView(view)
        dialogBuilder.setTitle(getString(R.string.select_category))
        val dialog: AlertDialog = dialogBuilder.create()
        dialog.show()
    }

    private fun pickImage() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                val bottomSheetDialogFragment: TedBottomPicker = TedBottomPicker.Builder(this@ExpenseDetailActivity)
                    .setOnImageSelectedListener { uri ->
                        Log.d("ted", "uri:" + uri)
                        Log.d("ted", "uri.path:" + uri.path)
                        selectedUri = uri

                        image_preview_expense_detail.visibility = View.VISIBLE
                        image_preview_expense_detail.post {
                            glideRequestManager
                                .load(uri)
                                .fitCenter()
                                .into(image_preview_expense_detail)
                        }

                        image_preview_expense_detail.layoutParams = LinearLayout.LayoutParams(800, 800)
                    }

                    .setSelectedUri(selectedUri)
                    .setPeekHeight(1200)
                    .create()
                bottomSheetDialogFragment.show(supportFragmentManager)
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                showPermissionDenied(deniedPermissions)
            }
        }

        TedPermission(this@ExpenseDetailActivity)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check()
        hideCursorOnNoteField()
    }

    private fun showPermissionDenied(deniedPermissions: ArrayList<String>?) {
        Toast.makeText(this@ExpenseDetailActivity, "Permission denied\n" +
            deniedPermissions.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun showCalendar() {
        val calendarDatePicker = CalendarDatePickerDialogFragment().setOnDateSetListener(this)
        calendarDatePicker.show(supportFragmentManager, FRAG_TAG_DATE_PICKER)
        hideCursorOnNoteField()
    }

    override fun onDateSet(dialog: CalendarDatePickerDialogFragment?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        calendar_result_text_expense_detail.text = getString(R.string.calendar_date_picker_result_values, year, monthOfYear + 1, dayOfMonth)
    }

    private fun showCursorOnNoteField() {
        expense_detail_note_field.isCursorVisible = true
    }

    private fun hideCursorOnNoteField() {
        expense_detail_note_field.isCursorVisible = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_expense_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_bar_save -> {
                val addedByTreasure = auth.currentUser?.email
                val amount = expense_detail_amount_field.text.toString()
                val category = expense_detail_categories_field.text
                val dateUpdated = calendar_result_text_expense_detail.text.toString()
                val expenseId = expense_detail_id_text_view.text.toString()
                val note = expense_detail_note_field.text.toString()

                updateExpense(addedByTreasure, amount.toInt(), category.toString(),
                    dateUpdated, expenseId, note)
            }
        }
        return true
    }

    private fun updateExpense(addedByTreasure: String?,
                              amount: Int?,
                              category: String?,
                              dateUpdated: String?,
                              expenseId: String?,
                              note: String?) {
        val dbUpdateExpenseRef = FirebaseDatabase
            .getInstance()
            .getReference("expenses")
            .child(expenseId)
        val expense = ExpenseModel(addedByTreasure, amount, category,
            dateUpdated, expenseId, note, null)
        dbUpdateExpenseRef.setValue(expense)
        Toast.makeText(applicationContext, getString(R.string.expense_updated), Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAGGG, "In the onRestart() event")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAGGG, "In the onResume() event")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAGGG, "In the onPause() event")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAGGG, "In the onStop() event")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAGGG, "In the onStart() event")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAGGG, "In the onDestroy() event")
    }
}
