package com.example.denip.nasyiatulaisyiyahfinance.setting

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import android.view.View
import com.example.denip.nasyiatulaisyiyahfinance.expense.category.CategoryExpenseSettingActivity
import com.example.denip.nasyiatulaisyiyahfinance.income.category.CategoryIncomeSettingActivity
import com.example.denip.nasyiatulaisyiyahfinance.login.ChangePasswordActivity
import com.example.denip.nasyiatulaisyiyahfinance.ExpandableListAdapter
import com.example.denip.nasyiatulaisyiyahfinance.login.LoginActivity
import com.example.denip.nasyiatulaisyiyahfinance.profile.ProfileActivity
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_setting.*
import kotlin.collections.HashMap

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var listAdapter: ExpandableListAdapter? = null
    private lateinit var listDataHeader: MutableList<String>
    private lateinit var listDataChild: HashMap<String, List<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initLayout()
        initAuth()
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

    private fun initLayout() {
        initToolbar()
        setting_profile.setOnClickListener(this)
        setting_change_password.setOnClickListener(this)
        setting_sign_out.setOnClickListener(this)
        initExpandableListView()
    }

    private fun initExpandableListView() {
        prepareListDataCategory()
        listAdapter = ExpandableListAdapter(this, listDataHeader, listDataChild)

        lvExp?.setAdapter(listAdapter)

        lvExp?.setOnGroupClickListener { parent, v, groupPosition, id ->
            false
        }

        // Listview Group expanded listener
        /*        lvExp?.setOnGroupExpandListener { groupPosition ->
            Toast.makeText(applicationContext, listDataHeader[groupPosition] + " Expanded",
                Toast.LENGTH_SHORT).show()
        }*/

        // Listview Group collasped listener
        /*lvExp?.setOnGroupCollapseListener { groupPosition ->
            Toast.makeText(applicationContext,
                listDataHeader[groupPosition] + " Collapsed",
                Toast.LENGTH_SHORT).show()
        }*/

        // Listview on child click listener
        lvExp?.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            when (childPosition) {
                0 -> launchCategoryExpenseSetting()
                1 -> launchCategoryIncomeSetting()
            }
            false
            /*            Toast.makeText(
                    applicationContext,
                    listDataHeader[groupPosition]
                        + " : "
                        + listDataChild[listDataHeader[groupPosition]]!![childPosition], Toast.LENGTH_SHORT)
                    .show()*/
        }
    }

    private fun prepareListDataCategory() {
        listDataHeader = ArrayList()
        listDataChild = HashMap()

        listDataHeader.add(getString(R.string.category_title))

        val categories = ArrayList<String>()
        categories.add(getString(R.string.expense))
        categories.add(getString(R.string.income))

        listDataChild.put(listDataHeader[0], categories)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_setting_layout)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.setting_profile -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.setting_change_password -> startActivity(Intent(this, ChangePasswordActivity::class.java))
            R.id.setting_sign_out -> {
                showDialogSignOut()
            }
        }
    }

    private fun launchCategoryIncomeSetting() {
        startActivity(Intent(this, CategoryIncomeSettingActivity::class.java))
    }

    private fun launchCategoryExpenseSetting() {
        startActivity(Intent(this, CategoryExpenseSettingActivity::class.java))
    }

    private fun showDialogSignOut() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setTitle(R.string.confirmation)
            .setMessage(R.string.confirmation_sign_out)
            .setPositiveButton(R.string.yes, { dialog, which ->
                auth.signOut()
            })
            .setNegativeButton(R.string.no, { dialog, _ ->
                dialog.cancel()
            })
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}
