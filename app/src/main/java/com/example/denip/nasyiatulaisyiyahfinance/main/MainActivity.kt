package com.example.denip.nasyiatulaisyiyahfinance.main

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.os.Environment
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import android.widget.Toast
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment
import com.example.denip.nasyiatulaisyiyahfinance.expense.ExpenseFragment
import com.example.denip.nasyiatulaisyiyahfinance.expense.ExpenseModel
import com.example.denip.nasyiatulaisyiyahfinance.income.IncomeFragment
import com.example.denip.nasyiatulaisyiyahfinance.login.LoginActivity
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.example.denip.nasyiatulaisyiyahfinance.about.AboutActivity
import com.example.denip.nasyiatulaisyiyahfinance.expense.category.CategoryExpenseSettingActivity
import com.example.denip.nasyiatulaisyiyahfinance.income.category.CategoryIncomeSettingActivity
import com.example.denip.nasyiatulaisyiyahfinance.login.ChangePasswordActivity
import com.example.denip.nasyiatulaisyiyahfinance.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sort_by_date_layout.view.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.supercsv.cellprocessor.constraint.NotNull
import org.supercsv.cellprocessor.ift.CellProcessor
import org.supercsv.io.CsvMapWriter
import org.supercsv.io.ICsvMapWriter
import org.supercsv.prefs.CsvPreference
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity(), ExpenseFragment.OnFragmentInteractionListener,
    IncomeFragment.OnFragmentInteractionListener, CalendarDatePickerDialogFragment.OnDateSetListener {

    override fun onFragmentInteraction(uri: Uri) {
        Log.d(HUNTR, "onFragmentInteraction")

    }

    companion object {
        private val HUNTR = "huntr_MainActivity"
        private var sectionsPagerAdapter: SectionsPagerAdapter? = null
        private val auth: FirebaseAuth = FirebaseAuth.getInstance()
        private val databaseExpenseRef = FirebaseDatabase.getInstance().getReference("expenses")
        private val databaseIncomeRef = FirebaseDatabase.getInstance().getReference("incomes")
        private lateinit var userFullNames: ArrayList<String>
        private lateinit var dateCreated: ArrayList<String>
        private lateinit var categories: ArrayList<String>
        private lateinit var amounts: ArrayList<String>
        private lateinit var notes: ArrayList<String>
        private lateinit var data: HashMap<String, String>
        private lateinit var arrayListHashMap: ArrayList<HashMap<String, String>>
        private val CSV_HEADER = arrayOf<String>(
            "Date",
            "Created_by",
            "Category",
            "Amount",
            "Note"
        )
        private lateinit var destinationFile: File
        private var isAlreadyExist = false
        private var toExport: String? = null
        private val currentDate = DateTime
            .now()
            .withZone(DateTimeZone.getDefault())
            .toLocalDateTime()
            .toDate()
        private var expenses: ArrayList<ExpenseModel> = arrayListOf()
        private var expense = ExpenseFragment()
        private var income = IncomeFragment()
        private val FRAG_TAG_DATE_PICKER = "fragment_date_picker_name_main"
        private lateinit var pickDateInflater: LayoutInflater
        private lateinit var view: View
        private lateinit var dateToSet: String
        private lateinit var type: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewPager()
        initAuth()
        Log.d(HUNTR + "REF", databaseExpenseRef.toString())

        Log.d(HUNTR, expenses.toString())
        Log.d(HUNTR, "In the onCreate() event")
        data = HashMap<String, String>()
        userFullNames = ArrayList<String>()
        dateCreated = ArrayList<String>()
        categories = ArrayList<String>()
        amounts = ArrayList<String>()
        notes = ArrayList<String>()
        arrayListHashMap = ArrayList<HashMap<String, String>>()
    }

    override fun onStart() {
        super.onStart()

        Log.d(HUNTR, expenses.toString())
        Log.d(HUNTR, "In the onStart() event")
        initToolbar()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_more_profile -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.menu_more_change_password -> startActivity(Intent(this, ChangePasswordActivity::class.java))
            R.id.menu_more_sign_out -> showDialogSignOut()
            R.id.menu_expense_category -> startActivity(Intent(this, CategoryExpenseSettingActivity::class.java))
            R.id.menu_income_category -> startActivity(Intent(this, CategoryIncomeSettingActivity::class.java))
            R.id.menu_expense_export -> showDialogExportExpense()
            R.id.menu_income_export -> showDialogExportIncome()
            R.id.menu_expense_show -> {
                type = "expense"
                showDialogShowTransaction()
            }
            R.id.menu_expense_sort_by -> {
                type = "expense"
                showDialogSortBy()
            }
            R.id.menu_income_show -> {
                type = "income"
                showDialogShowTransaction()
            }
            R.id.menu_income_sort_by -> {
                type = "income"
                showDialogSortBy()
            }
            R.id.menu_more_about -> startActivity(Intent(this, AboutActivity::class.java))
        }
        return true
    }

    private fun showDialogSortBy() {
        val dialogBuilder = AlertDialog.Builder(this)
        val choices = arrayOf<CharSequence>(getString(R.string.amount), getString(R.string.date))
        var checked = ""

        dialogBuilder
            .setTitle(getString(R.string.sort_by))
            .setSingleChoiceItems(choices, -1, { dialog, item ->
                when (item) {
                    0 -> checked = "amount"
                    1 -> checked = "date"
                }
            })
            .setPositiveButton(getString(R.string.next), { dialog, which ->
                Log.d(HUNTR, "type : " + type)
                Log.d(HUNTR, "checked : " + checked)
                when (checked) {
                    "amount" -> showDialogSortByAmount()
                    "date" -> showDialogPickDate()
                }
            })
            .create()
            .show()
    }

    private fun showDialogSortByAmount() {
        val dialogBuilder = AlertDialog.Builder(this)
        val choices = arrayOf<CharSequence>(
            getString(R.string.lowest_to_highest),
            getString(R.string.highest_to_lowest)
        )
        var checked = ""

        dialogBuilder
            .setTitle(getString(R.string.sort_by_amount))
            .setSingleChoiceItems(choices, -1, { dialog, item ->
                when (item) {
                    0 -> checked = "lowToHi"
                    1 -> checked = "hiToLow"
                }
            })
            .setPositiveButton(getString(R.string.ok), { dialog, which ->
                Log.d(HUNTR, "type : " + type)
                Log.d(HUNTR, "checked : " + checked)

                when (type) {
                    "expense" -> {
                        when (checked) {
                            "lowToHi" -> expense.sortExpenseAmountLowestToHighest()
                            "hiToLow" -> expense.sortExpenseAmountHighestToLowest()
                        }
                    }
                    "income" -> {
                        when (checked) {
                            "lowToHi" -> income.sortIncomeAmountLowestToHighest()
                            "hiToLow" -> income.sortIncomeAmountHighestToLowest()
                        }
                    }
                }

            })
            .create()
            .show()
    }

    private fun showDialogPickDate() {
        val dialogBuilder = AlertDialog.Builder(this)
        pickDateInflater = layoutInflater
        view = pickDateInflater.inflate(R.layout.sort_by_date_layout, null)

        view.sort_by_date_from.isFocusable = false
        view.sort_by_date_from.setOnClickListener {
            dateToSet = "from"
            showCalendar()
        }

        view.sort_by_date_to.isFocusable = false
        view.sort_by_date_to.setOnClickListener {
            dateToSet = "to"
            showCalendar()
        }

        Log.d(HUNTR, "type : " + type)
        dialogBuilder
            .setView(view)
            .setTitle(getString(R.string.sort))
            .setPositiveButton(getString(R.string.ok), { dialog, which ->
                val fromDate = view.sort_by_date_from?.text.toString()
                val toDate = view.sort_by_date_to?.text.toString()
                when {
                    fromDate.isEmpty() -> Toast.makeText(this, getString(R.string.prompt_empty_field),
                        Toast.LENGTH_LONG).show()
                    toDate.isEmpty() -> Toast.makeText(this, getString(R.string.prompt_empty_field),
                        Toast.LENGTH_LONG).show()
                    else -> {
                        when (type) {
                            "expense" -> expense.sortExpenseByDate(fromDate, toDate)
                            "income" -> income.sortIncomeByDate(fromDate, toDate)
                        }


                    }
                }
            })
            .create()
            .show()
    }

    private fun showCalendar() {
        val calendarDatePicker = CalendarDatePickerDialogFragment().setOnDateSetListener(this)
        calendarDatePicker.show(supportFragmentManager, FRAG_TAG_DATE_PICKER)
    }

    override fun onDateSet(dialog: CalendarDatePickerDialogFragment?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        when (dateToSet) {
            "from" -> view.sort_by_date_from?.setText(getString(R.string.calendar_date_picker_result_values,
                year, monthOfYear + 1, dayOfMonth))
            "to" -> view.sort_by_date_to?.setText(getString(R.string.calendar_date_picker_result_values,
                year, monthOfYear + 1, dayOfMonth))
        }

    }

    private fun showDialogShowTransaction() {
        Log.d(HUNTR, "In the showDialogShowTransaction()")
        val dialogBuilder = AlertDialog.Builder(this)
        val choices = arrayOf<CharSequence>(getString(R.string.show_all), getString(R.string.show_only_me))
        var checked = ""

        dialogBuilder
            .setTitle(getString(R.string.show))
            .setSingleChoiceItems(choices, -1, { dialog, item ->
                when (item) {
                    0 -> checked = "all"
                    1 -> checked = "onlyme"
                }
            })
            .setPositiveButton(getString(R.string.ok), { dialog, which ->
                Log.d(HUNTR, "type : " + type)
                Log.d(HUNTR, "checked : " + checked)
                when (type) {
                    "expense" -> {
                        when (checked) {
                            "all" -> expense.showExpenseAll()
                            "onlyme" -> expense.showExpenseOnlyMe()
                        }
                    }
                    "income" -> {
                        when (checked) {
                            "all" -> income.showIncomeAll()
                            "onlyme" -> income.showIncomeOnlyMe()
                        }
                    }
                }

            })
            .create()
            .show()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_main_layout)
    }

    private fun showDialogExportExpense() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setTitle(R.string.confirmation)
            .setMessage(R.string.confirmation_export_expense)
            .setPositiveButton(R.string.yes, { dialog, _ ->
                toExport = "expense"
                exportToCsv()
                showDialogLocation()

            })
            .setNegativeButton(R.string.no, { dialog, _ ->
                dialog.cancel()
            })
            .show()
    }

    private fun showDialogLocation() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setTitle("Location")
            .setMessage("" + destinationFile.toString())
            .show()
    }

    private fun showDialogExportIncome() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setTitle(R.string.confirmation)
            .setMessage(R.string.confirmation_export_income)
            .setPositiveButton(R.string.yes, { dialog, _ ->
                toExport = "income"
                exportToCsv()
                Toast.makeText(this, "Make your export expense action.", Toast.LENGTH_SHORT).show()
            })
            .setNegativeButton(R.string.no, { dialog, _ ->
                dialog.cancel()
            })
            .show()
    }

    private fun exportToCsv() {
        addData()
        sdCardHandler()
        writeDataOnCSV()
    }

    private fun writeDataOnCSV() {
        Log.d(HUNTR, "In writeDataOnCSV() event")
        Log.d(HUNTR, "toExport = " + toExport)
        var mapWriter: ICsvMapWriter? = null
        when (toExport) {
            "expense" -> {
                try {
                    mapWriter = CsvMapWriter(FileWriter(destinationFile, true),
                        CsvPreference.STANDARD_PREFERENCE)

                    val processors: Array<CellProcessor> = getProcessors()

                    //write the header
                    if (!isAlreadyExist) mapWriter.writeHeader(*CSV_HEADER)

                    if (data.size > 0) {
                        for (n in arrayListHashMap) {
                            mapWriter.write(n, CSV_HEADER, processors)
                        }
                    }
                    Log.d(HUNTR, "CSV_HEADER : " + CSV_HEADER[0])
                    Log.d(HUNTR, "CSV_HEADER size : " + CSV_HEADER.size)
                    Log.d(HUNTR, "processors size : " + processors.size)
                    Log.d(HUNTR, "data in writeDataOnCSV() : " + data)
                    Log.d(HUNTR, "data.values.size in writeDataOnCSV() : " + data.values.size)
                    Log.d(HUNTR, "data.keys.size in writeDataOnCSV() : " + data.keys.size)

                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    if (mapWriter != null) {
                        try {
                            mapWriter.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            "income" -> {

            }
        }
        Log.d(HUNTR, "-End of writeDataOnCSV()-")
    }

    private fun getProcessors(): Array<CellProcessor> {
        val processors = arrayOf<CellProcessor>(
            NotNull(), NotNull(), NotNull(), NotNull(), NotNull()
        )
        return processors
    }

    private fun sdCardHandler() {
        when (toExport) {
            "expense" -> {
                val mainDirect = File("" + Environment.getExternalStorageDirectory()
                    + "/Android/data/" + packageName)
                Log.d(HUNTR, "mainDirect : " + mainDirect.toString())

                if (!mainDirect.exists()) mainDirect.mkdirs()
                Log.d(HUNTR, "mainDirect.exist : " + mainDirect.exists())

                destinationFile = File("" + mainDirect + "/NA_Finance_Expense_" + currentDate.toString() + ".csv")

                Log.d(HUNTR, "destinationFile : " + destinationFile.toString())
                if (destinationFile.exists()) isAlreadyExist = true

                Log.d(HUNTR, "destinationFile.exist : " + destinationFile.exists())
            }
            "income" -> {
                val mainDirect = File("" + Environment.getExternalStorageDirectory()
                    + "/Android/data/" + packageName)
                if (!mainDirect.exists()) mainDirect.mkdir()

                destinationFile = File("" + mainDirect + "/NA_Finance_Income_" + currentDate.toString() + ".csv")

                if (destinationFile.exists()) isAlreadyExist = true
            }
        }
        Log.d(HUNTR, "-End of sdCardHandler()-")
    }

    private fun addData() {
        Log.d(HUNTR, "addData In the addData() event")
        Log.d(HUNTR, "toExport = " + toExport)

        when (toExport) {
            "expense" -> {
                Log.d(HUNTR, "data in addData() : " + data)
                Log.d(HUNTR, "data.values.size in addData() : " + data.values.size)
                Log.d(HUNTR, "data.keys.size in addData() : " + data.keys.size)

                databaseExpenseRef.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(databaseError: DatabaseError?) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        userFullNames.clear()
                        dateCreated.clear()
                        categories.clear()
                        amounts.clear()
                        notes.clear()

                        dataSnapshot!!.children
                            .map { it?.child("addedByTreasurer")?.value.toString().replace("^", "\n") }
                            .forEach {
                                userFullNames.add(it)
                            }

                        dataSnapshot.children
                            .map { it?.child("dateCreated")?.value.toString().replace("^", "\n") }
                            .forEach {
                                dateCreated.add(it)
                            }

                        dataSnapshot.children
                            .map { it?.child("category")?.value.toString().replace("^", "\n") }
                            .forEach {
                                categories.add(it)
                            }

                        dataSnapshot.children
                            .map { it?.child("amount")?.value.toString().plus("\n") }
                            .forEach {
                                amounts.add(it)
                            }

                        dataSnapshot.children
                            .map { it?.child("note")?.value.toString().replace("^", "\n") }
                            .forEach {
                                notes.add(it)
                            }
                    }
                })

                Log.d(HUNTR, "userFullNames : " + userFullNames)
                Log.d(HUNTR, "userFullNames size : " + userFullNames.size)

                if (dateCreated.size > 0) {
                    var i = 0

                    for (n in dateCreated) {
                        data = HashMap()
                        data.put(CSV_HEADER[0], dateCreated[i])
                        data.put(CSV_HEADER[1], userFullNames[i])
                        data.put(CSV_HEADER[2], categories[i])
                        data.put(CSV_HEADER[3], amounts[i])
                        data.put(CSV_HEADER[4], notes[i])
                        i++
                        arrayListHashMap.add(data)
                    }
                }
            }
            "income" -> {

            }
        }
        Log.d(HUNTR, "-End of addData()-")
    }

    private fun showDialogSignOut() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setTitle(R.string.confirmation)
            .setMessage(R.string.confirmation_sign_out)
            .setPositiveButton(R.string.yes, { dialog, which ->
                signOut()
            })
            .setNegativeButton(R.string.no, { dialog, _ ->
                dialog.cancel()
            })
            .show()
    }

    private fun signOut() {
        auth.signOut()
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        showLogoutProgress()
    }

    private fun showLogoutProgress() {
        Log.d(HUNTR, "In the showLogoutProgress")
        logout_progress_layout.visibility = View.VISIBLE
        logout_progress.visibility = View.VISIBLE
    }

    private fun initViewPager() {
        Log.d(HUNTR, "In the initViewPager() event")
        sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        container!!.adapter = sectionsPagerAdapter

        container!!.currentItem = 0

        tab_layout.setupWithViewPager(container)
        toolbar_main_layout.title = getString(R.string.app_name_long)
        Log.d(HUNTR, "viewPager currentItem : " + container!!.currentItem)
    }

    private fun initAuth() {
        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user: FirebaseUser? = firebaseAuth.currentUser

            if (user == null) {
                launchLoginActivity()
            }
        }

        auth.addAuthStateListener(authListener)
    }

    private fun launchLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private val tabTitles = arrayOf(getString(R.string.expense), getString(R.string.income))
        override fun getItem(position: Int): Fragment {
            Log.d(HUNTR, "In the getItem() event")
            when (position) {
                0 -> return ExpenseFragment()
                1 -> return IncomeFragment()
            }
            Log.d(HUNTR, "position : " + position)
            return MainFragment.newInstance(position)
        }

        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence? {
            Log.d(HUNTR, "In the getPageTitle() event")
            when (position) {
                0 -> return getString(R.string.expense)
                1 -> return getString(R.string.income)
            }
            Log.d(HUNTR, "position : " + position)
            return null
        }

        override fun getItemPosition(`object`: Any?): Int {
            Log.d(HUNTR, "In the getItemPosition()")
            return PagerAdapter.POSITION_NONE
        }
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(HUNTR, "In the onRestart() event")
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

    override fun onAttachFragment(fragment: android.app.Fragment?) {
        super.onAttachFragment(fragment)
        Log.d(HUNTR, "In the onAttachFragment()")
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        Log.d(HUNTR, "In the onResumeFragment()")
    }


}
