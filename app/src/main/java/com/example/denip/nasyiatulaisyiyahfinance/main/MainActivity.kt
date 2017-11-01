package com.example.denip.nasyiatulaisyiyahfinance.main

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.denip.nasyiatulaisyiyahfinance.expense.ExpenseFragment
import com.example.denip.nasyiatulaisyiyahfinance.expense.ExpenseModel
import com.example.denip.nasyiatulaisyiyahfinance.income.IncomeFragment
import com.example.denip.nasyiatulaisyiyahfinance.login.LoginActivity
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.example.denip.nasyiatulaisyiyahfinance.expense.category.CategoryExpenseSettingActivity
import com.example.denip.nasyiatulaisyiyahfinance.income.category.CategoryIncomeSettingActivity
import com.example.denip.nasyiatulaisyiyahfinance.login.ChangePasswordActivity
import com.example.denip.nasyiatulaisyiyahfinance.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import me.relex.circleindicator.CircleIndicator
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

class MainActivity : AppCompatActivity(), ExpenseFragment.OnFragmentInteractionListener,
    IncomeFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {
        Log.d(HUNTR, "onFragmentInteraction")

    }

    companion object {
        private val HUNTR = "huntr_MainActivity"
        private var sectionsPagerAdapter: SectionsPagerAdapter? = null
        private var viewPager: ViewPager? = null
        private var indicator: CircleIndicator? = null
        private val auth: FirebaseAuth = FirebaseAuth.getInstance()
        private val databaseExpenseRef = FirebaseDatabase.getInstance().getReference("expenses")
        private val databaseIncomeRef = FirebaseDatabase.getInstance().getReference("incomes")
        private lateinit var userFullNames: ArrayList<String>
        private lateinit var dateCreated: ArrayList<String>
        private lateinit var categories: ArrayList<String>
        private lateinit var amounts: ArrayList<String>
        private lateinit var notes: ArrayList<String>
        //private lateinit var expenses: ArrayList<ExpenseModel>
        //private var expenses = mutableListOf<ExpenseModel>()
        private lateinit var data: HashMap<String, Any>
        private lateinit var userFullNamesOne: String
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
        //private val storage = FirebaseStorage.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewPager()
        initAuth()
        //expenses = ArrayList<ExpenseModel>()
        Log.d(HUNTR + "REF", databaseExpenseRef.toString())

        Log.d(HUNTR, expenses.toString())
        Log.d(HUNTR, "In the onCreate() event")
        data = HashMap<String, Any>()
        userFullNames = ArrayList<String>()
        dateCreated = ArrayList<String>()
        categories = ArrayList<String>()
        amounts = ArrayList<String>()
        notes = ArrayList<String>()
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
            R.id.setting_profile -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.setting_change_password -> startActivity(Intent(this, ChangePasswordActivity::class.java))
            R.id.setting_sign_out -> showDialogSignOut()
            R.id.setting_category_expense -> startActivity(Intent(this, CategoryExpenseSettingActivity::class.java))
            R.id.setting_category_income -> startActivity(Intent(this, CategoryIncomeSettingActivity::class.java))
            R.id.setting_export_expense -> showDialogExportExpense()
            R.id.setting_export_income -> showDialogExportIncome()
        }
        return true
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
            .setMessage(R.string.confirmation_export_expense)
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

                    mapWriter.write(data, CSV_HEADER, processors)
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
                            .map { it?.child("addedByTreasure")?.value.toString().replace("^", "\n") }
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
                            .map { it?.child("amount")?.value.toString().replace("^", "\n") }
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

                data.put(CSV_HEADER[0], dateCreated)
                data.put(CSV_HEADER[1], userFullNames)
                data.put(CSV_HEADER[2], categories)
                data.put(CSV_HEADER[3], amounts)
                data.put(CSV_HEADER[4], notes)
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
                auth.signOut()
                //showLogoutProgress()
            })
            .setNegativeButton(R.string.no, { dialog, _ ->
                dialog.cancel()
            })
            .show()
    }

    private fun initViewPager() {
        Log.d(HUNTR, "In the initViewPager() event")
        sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        viewPager = findViewById(R.id.container) as ViewPager
        indicator = findViewById(R.id.dotsIndicator) as CircleIndicator
        viewPager!!.adapter = sectionsPagerAdapter
        indicator!!.setViewPager(viewPager)

        viewPager!!.currentItem = 0
        Log.d(HUNTR, "viewPager currentItem : " + viewPager!!.currentItem)
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
                0 -> return "SECTION 1"
                1 -> return "SECTION 2"
                2 -> return "SECTION 3"
            }
            Log.d(HUNTR, "position : " + position)
            return null
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
