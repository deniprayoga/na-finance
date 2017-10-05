package com.example.denip.nasyiatulaisyiyahfinance.income

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.example.denip.nasyiatulaisyiyahfinance.income.category.PickCategoryIncomeActivity
import com.example.denip.nasyiatulaisyiyahfinance.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedbottompicker.TedBottomPicker
import kotlinx.android.synthetic.main.activity_add_amount_income.view.*
import kotlinx.android.synthetic.main.activity_add_income.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.ArrayList

class AddIncomeActivity : AppCompatActivity(), View.OnClickListener, CalendarDatePickerDialogFragment.OnDateSetListener {

    companion object {
        val FRAG_TAG_DATE_PICKER = "fragment_date_picker_name"
        var selectedUri: Uri? = null
        lateinit var glideRequestManager: RequestManager
        private val auth = FirebaseAuth.getInstance()
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
                finish()
                launchLoginActivity()
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
        calendar_button_income.setOnClickListener(this)
        calendar_result_text_income.setOnClickListener(this)
        income_amount_field.setOnClickListener(this)
        pick_image_button_income.setOnClickListener(this)
        income_note_field.setOnClickListener(this)
        income_categories_field.setOnClickListener(this)
    }

    private fun showCurrentDate() {
        val currentDate = DateTime.now().withZone(DateTimeZone.getDefault()).toString("dd-MM-yyyy")
        calendar_result_text_income.text = currentDate.toString()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_add_income_layout)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.calendar_button_income -> showCalendar()
            R.id.calendar_result_text_income -> showCalendar()
            R.id.income_amount_field -> showAddAmountDialog()
            R.id.pick_image_button_income -> pickImage()
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
        val dialog: AlertDialog = dialogBuilder.create()
        dialog.show()
        hideCursorOnNoteField()
    }

    private fun hideCursorOnNoteField() {
        income_note_field.isCursorVisible = false
    }

    private fun showCursorOnNoteField() {
        income_note_field?.isCursorVisible = true
    }

    private fun pickImage() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                val bottomSheetDialogFragment: TedBottomPicker = TedBottomPicker.Builder(this@AddIncomeActivity)
                    .setOnImageSelectedListener { uri ->
                        Log.d("ted", "uri:" + uri)
                        Log.d("uri", "uri.path:" + uri.path)
                        selectedUri = uri

                        image_preview_income.visibility = View.VISIBLE
                        image_preview_income.post {
                            glideRequestManager
                                .load(uri)
                                .fitCenter()
                                .into(image_preview_income)
                        }
                        image_preview_income.layoutParams = LinearLayout.LayoutParams(800, 800)
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

        TedPermission(this@AddIncomeActivity)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check()
    }

    private fun showPermissionDenied(deniedPermissions: ArrayList<String>?) {
        Toast.makeText(this@AddIncomeActivity, "Permission denied\n" +
            deniedPermissions.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun showCalendar() {
        val calendarDatePicker = CalendarDatePickerDialogFragment().setOnDateSetListener(this)
        calendarDatePicker.show(supportFragmentManager, FRAG_TAG_DATE_PICKER)
    }

    override fun onDateSet(dialog: CalendarDatePickerDialogFragment?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        calendar_result_text_income.text = getString(R.string.calendar_date_picker_result_values, year, monthOfYear + 1, dayOfMonth)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
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
        val notePhotoUri = selectedUri.toString()
        val category = income_categories_field?.text.toString()

        when {
            amount.isEmpty() -> income_amount_field.error = getString(R.string.prompt_amount_empty)
            note.isEmpty() -> income_note_field.error = getString(R.string.prompt_note_empty)
            category.isEmpty() -> income_categories_field.error = getString(R.string.prompt_category_empty)
            else -> {
                val id: String? = dbAddIncomeRef?.push()?.key
                val income = IncomeModel(id, auth.currentUser?.email, amount.toInt(), category,
                    dateCreated, note, notePhotoUri)
                dbAddIncomeRef?.child(id)?.setValue(income)
                Log.d("incomeeeeee", income.toString())
                Toast.makeText(this, getString(R.string.income_added), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
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
