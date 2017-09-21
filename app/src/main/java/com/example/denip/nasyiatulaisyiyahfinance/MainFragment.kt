package com.example.denip.nasyiatulaisyiyahfinance

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_main.view.*

/**
 * Created by denip on 9/15/2017.
 */

class MainFragment : Fragment(), View.OnClickListener {

    companion object {

        private val ARG_SECTION_NUMBER = "section_number"

        fun newInstance(sectionNumber: Int): MainFragment {
            val fragment = MainFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragmentMainView = inflater!!.inflate(R.layout.fragment_main, container, false)
        val fragmentExpenseView = inflater.inflate(R.layout.fragment_expense, container, false)
        val fragmentIncomeView = inflater.inflate(R.layout.fragment_income, container, false)

        fragmentMainView.setting_button.setOnClickListener(this)

        when {
            arguments.getInt(ARG_SECTION_NUMBER) == 0 -> return fragmentExpenseView
            arguments.getInt(ARG_SECTION_NUMBER) == 1 -> return fragmentMainView
            arguments.getInt(ARG_SECTION_NUMBER) == 2 -> return fragmentIncomeView
        }

        initLayout()
        initAuth()
        return null
    }

    private fun initLayout() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.setting_button -> startActivity(Intent(context, SettingActivity::class.java))
        }
    }

    private fun launchLoginActivity() {
        startActivity(Intent(context, LoginActivity::class.java))
    }

    private fun initAuth() {
        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user: FirebaseUser? = firebaseAuth.currentUser

            if (user == null) {
                activity.supportFragmentManager.beginTransaction().remove(this).commit()
                launchLoginActivity()
            }
        }

        auth.addAuthStateListener(authListener)
    }
}