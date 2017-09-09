package com.example.denip.nasyiatulaisyiyahfinance

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedbottompicker.TedBottomPicker
import kotlinx.android.synthetic.main.activity_add_expense.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.ArrayList

class AddExpenseActivity : AppCompatActivity(), View.OnClickListener,
    CalendarDatePickerDialogFragment.OnDateSetListener {
    companion object {
        val FRAG_TAG_DATE_PICKER = "fragment_date_picker_name"
        val context: Context? = null
        var selectedUri: Uri? = null
        lateinit var glideRequestManager: RequestManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)
        glideRequestManager = Glide.with(this)

        val bundle: Bundle? = intent!!.extras
        val amount: String? = bundle?.getString(getString(R.string.EXTRA_AMOUNT))
        expense_amount_field.setText(amount)
        initLayout()
    }

    private fun initLayout() {
        expense_amount_field.isFocusable = false
        initToolbar()
        showCurrentDate()
        calendar_button_expense.setOnClickListener(this)
        pick_image_button_expense.setOnClickListener(this)
        expense_amount_field.setOnClickListener(this)

    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_add_expense_layout)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onDateSet(dialog: CalendarDatePickerDialogFragment?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        calendar_result_text_expense.text = getString(R.string.calendar_date_picker_result_values, year, monthOfYear + 1, dayOfMonth)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_bar_done -> finish()
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.clear_button -> {
                clearField()
            }
            R.id.calendar_button_expense -> {
                showCalendar()
            }
            R.id.pick_image_button_expense -> {
                pickImage()
            }
            R.id.expense_amount_field -> {
                navigateToAddAmount()
            }
        }
    }

    private fun navigateToAddAmount() {
        val intent = Intent(this, AddAmountExpenseActivity::class.java)
        val amount = expense_amount_field?.text.toString()
        intent.putExtra(getString(R.string.EXTRA_AMOUNT), amount)
        startActivity(intent)
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
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check()
    }

    private fun showPermissionDenied(deniedPermissions: ArrayList<String>?) {
        Toast.makeText(this@AddExpenseActivity, "Permission denied\n" +
            deniedPermissions.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun showCalendar() {
        val calendarDatePicker = CalendarDatePickerDialogFragment().setOnDateSetListener(this)
        calendarDatePicker.show(supportFragmentManager, FRAG_TAG_DATE_PICKER)
    }

    private fun showCurrentDate() {
        val currentDate = DateTime.now().withZone(DateTimeZone.getDefault()).toString("dd-MM-yyyy")
        calendar_result_text_expense.text = currentDate.toString()
    }

    private fun clearField() {
        expense_amount_field.text.clear()
    }

}