package com.example.denip.nasyiatulaisyiyahfinance.income

import android.Manifest
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
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedbottompicker.TedBottomPicker
import kotlinx.android.synthetic.main.activity_add_income.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.ArrayList

class AddIncomeActivity : AppCompatActivity(), View.OnClickListener, CalendarDatePickerDialogFragment.OnDateSetListener {

    companion object {
        val FRAG_TAG_DATE_PICKER = "fragment_date_picker_name"
        var selectedUri: Uri? = null
        lateinit var glideRequestManager: RequestManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_income)
        glideRequestManager = Glide.with(this)

        val bundle: Bundle? = intent!!.extras
        val amount: String? = bundle?.getString(getString(R.string.EXTRA_AMOUNT))
        income_amount_field.setText(amount)
        initLayout()
    }

    private fun initLayout() {
        income_amount_field.isFocusable = false
        initToolbar()
        showCurrentDate()
        calendar_button_income.setOnClickListener(this)
        calendar_result_text_income.setOnClickListener(this)
        income_amount_field.setOnClickListener(this)
        pick_image_button_income.setOnClickListener(this)
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
            R.id.income_amount_field -> navigateToAddAmount()
            R.id.pick_image_button_income -> pickImage()
        }
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

    private fun navigateToAddAmount() {
        val intent = Intent(this, AddAmountIncomeActivity::class.java)
        val amount = income_amount_field.text.toString()
        intent.putExtra(getString(R.string.EXTRA_AMOUNT), amount)
        startActivity(intent)
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
            R.id.action_bar_done -> finish()
            android.R.id.home -> onBackPressed()
        }
        return true
    }
}
