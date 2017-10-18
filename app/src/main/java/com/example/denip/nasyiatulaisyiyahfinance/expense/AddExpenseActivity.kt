package com.example.denip.nasyiatulaisyiyahfinance.expense

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment
import com.example.denip.nasyiatulaisyiyahfinance.login.LoginActivity
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.example.denip.nasyiatulaisyiyahfinance.expense.category.PickCategoryExpenseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedbottompicker.TedBottomPicker
import kotlinx.android.synthetic.main.activity_add_amount_expense.view.*
import kotlinx.android.synthetic.main.activity_add_expense.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import kotlin.collections.ArrayList

class AddExpenseActivity : AppCompatActivity(), View.OnClickListener,
    CalendarDatePickerDialogFragment.OnDateSetListener {

    companion object {
        private val HUNTR = "huntr_AddExpense"
        private val FRAG_TAG_DATE_PICKER = "fragment_date_picker_name"
        private var selectedUri: Uri? = null
        private lateinit var glideRequestManager: RequestManager
        private val auth = FirebaseAuth.getInstance()
        private val dbRef = FirebaseDatabase.getInstance().reference
        private val context: Context? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)
        glideRequestManager = Glide.with(this)

        initLayout()
        initAuth()
        Log.d(HUNTR, "In the onCreate() event")
    }

    private fun initLayout() {
        expense_note_field.isCursorVisible = false
        expense_amount_field.isCursorVisible = false
        expense_amount_field.isFocusable = false
        expense_categories_field.isFocusable = false
        initToolbar()
        showCurrentDate()
        calendar_button_expense.setOnClickListener(this)
        calendar_result_text_expense.setOnClickListener(this)
        pick_image_button_expense.setOnClickListener(this)
        image_preview_expense.setOnClickListener(this)
        layout_pick_image_expense.setOnClickListener(this)
        expense_amount_field.setOnClickListener(this)
        expense_categories_field.setOnClickListener(this)
        expense_note_field.setOnClickListener(this)
        Log.d(HUNTR, "In initLayout()")
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
        Log.d(HUNTR, "In initAuth()")
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_add_expense_layout)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Log.d(HUNTR, "In initToolbar()")
    }

    override fun onDateSet(dialog: CalendarDatePickerDialogFragment?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        Log.d(HUNTR, "In onDateSet()")
        calendar_result_text_expense.text = getString(R.string.calendar_date_picker_result_values, year, monthOfYear + 1, dayOfMonth)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Log.d(HUNTR, "In onOptionItemSelected")
        when (item?.itemId) {
            R.id.action_bar_done -> {
                saveExpense()
            }
            android.R.id.home -> finish()
        }
        return true
    }

    private fun saveExpense() {
        Log.d(HUNTR, "In saveExpense()")
        val dateCreated = calendar_result_text_expense?.text.toString()
        val amount = expense_amount_field?.text.toString()
        val note = expense_note_field?.text.toString()
        val notePhotoUri = selectedUri.toString()
        val category = expense_categories_field?.text.toString()

        when {
            amount.isEmpty() -> expense_amount_field.error = getString(R.string.prompt_amount_empty)
            note.isEmpty() -> expense_note_field.error = getString(R.string.prompt_note_empty)
            category.isEmpty() -> expense_categories_field.error = getString(R.string.prompt_category_empty)
            else -> {

                val expenseId: String? = dbRef?.push()?.key

                //prefs get from PickCategoryExpenseAdapter class
                val prefs = PreferenceManager.getDefaultSharedPreferences(this@AddExpenseActivity)
                val categoryId = prefs.getString(getString(R.string.CATEGORY_ID_EXPENSE), "")

                val dbCurrentUserFullnameRef = dbRef?.child("users")
                Log.d(HUNTR, "path current user fullName : " + dbCurrentUserFullnameRef.toString())
                dbCurrentUserFullnameRef?.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(databaseError: DatabaseError?) {
                        Log.d(HUNTR, "In onCancelled()")
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        val fullName = dataSnapshot?.child(auth.currentUser?.uid)?.child("fullName")
                            ?.value.toString()
                        Log.d(HUNTR, "fullName : " + fullName)

                        val expense = ExpenseModel(fullName, amount.toInt(), category, dateCreated,
                            expenseId, note, notePhotoUri, categoryId)

                        dbRef?.child("expenses")?.child(expenseId)?.setValue(expense)

                        Log.d(HUNTR, "expense amount : " + expense.amount)
                        Log.d(HUNTR, "expense note : " + expense.note)
                        Log.d(HUNTR, "expense note photo uri : " + expense.notePhotoUri)
                        Log.d(HUNTR, "expense added by treasurer : " + expense.addedByTreasure)
                        Log.d(HUNTR, "expense category : " + expense.category)
                        Log.d(HUNTR, "expense category id : " + expense.categoryId)
                        Log.d(HUNTR, "expense date created : " + expense.dateCreated)
                        Log.d(HUNTR, "expense id : " + expense.expenseId)
                    }
                })

                Toast.makeText(this, getString(R.string.expense_added), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_expense, menu)
        return true
    }

    private fun showAddAmountDialog() {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val addAmountInflater: LayoutInflater = layoutInflater
        val view: View = addAmountInflater.inflate(R.layout.activity_add_amount_expense, null)

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

        dialogBuilder
            .setView(view)
            .setTitle(getString(R.string.amount))
            .setPositiveButton(getString(R.string.ok), { dialog, _ ->
                run {
                    val currentAmount = view.expense_amount_field_add_amount?.text.toString()
                    expense_amount_field.text.clear()
                    expense_amount_field.setText(currentAmount)
                    dialog.dismiss()
                    expense_amount_field.error = null
                }
            })
        val dialog: AlertDialog = dialogBuilder.create()
        dialog.show()
        hideCursorOnNoteField()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.calendar_button_expense -> showCalendar()
            R.id.pick_image_button_expense -> pickImage()
            R.id.expense_amount_field -> showAddAmountDialog()
            R.id.calendar_result_text_expense -> showCalendar()
            R.id.expense_categories_field -> pickCategory()
            R.id.expense_note_field -> showCursorOnNoteField()
            R.id.image_preview_expense -> pickImage()
            R.id.layout_pick_image_expense -> pickImage()
        }
    }

    private fun pickCategory() {
        val intent = Intent(this@AddExpenseActivity, PickCategoryExpenseActivity::class.java)
        //intent.putExtra(getString(R.string.CATEGORY_NAME_EXPENSE), "selected_category")
        startActivityForResult(intent, 1)
    }

    private fun showCursorOnNoteField() {
        expense_note_field?.isCursorVisible = true
    }

    private fun hideCursorOnNoteField() {
        expense_note_field.isCursorVisible = false
    }

    private fun pickImage() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                val bottomSheetDialogFragment: TedBottomPicker = TedBottomPicker.Builder(this@AddExpenseActivity)
                    .setOnImageSelectedListener { uri ->
                        Log.d("ted", "uri:" + uri)
                        Log.d("ted", "uri.path:" + uri.path)
                        selectedUri = uri

                        image_preview_expense.visibility = View.VISIBLE
                        image_preview_expense.post {
                            glideRequestManager
                                .load(uri)
                                .fitCenter()
                                .into(image_preview_expense)
                        }

                        image_preview_expense.layoutParams = LinearLayout.LayoutParams(800, 800)
                        hint_pick_image?.visibility = View.GONE
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

        TedPermission(this@AddExpenseActivity)
            .setPermissionListener(permissionListener)
            .setDeniedMessage(getString(R.string.ted_permission_denied))
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check()
        hideCursorOnNoteField()
    }

    private fun showPermissionDenied(deniedPermissions: ArrayList<String>?) {
        Toast.makeText(this@AddExpenseActivity, "Permission denied\n" +
            deniedPermissions.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun launchLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun showCalendar() {
        val calendarDatePicker = CalendarDatePickerDialogFragment().setOnDateSetListener(this)
        calendarDatePicker.show(supportFragmentManager, FRAG_TAG_DATE_PICKER)
        hideCursorOnNoteField()
    }

    private fun showCurrentDate() {
        val currentDate = DateTime.now().withZone(DateTimeZone.getDefault()).toString("dd-MM-yyyy")
        calendar_result_text_expense.text = currentDate.toString()
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(HUNTR, "In the onRestart() event")

        //prefs get from PickCategoryExpenseAdapter
        val prefs = PreferenceManager.getDefaultSharedPreferences(this@AddExpenseActivity)
        val categoryName = prefs.getString(getString(R.string.CATEGORY_NAME_EXPENSE), "")
        val categoryNumber = prefs.getString(getString(R.string.CATEGORY_NUMBER_EXPENSE), "")
        Log.d(HUNTR, "selected category : " + categoryName)
        Log.d(HUNTR, "selected category number : " + categoryNumber)
        expense_categories_field?.error = null
        expense_categories_field?.setText("$categoryNumber $categoryName")
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
        Log.d(HUNTR, "In the onStart() event")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(HUNTR, "In the onDestroy() event")
    }
}