package com.example.denip.nasyiatulaisyiyahfinance

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

/**
 * Created by denip on 8/17/2017.
 */

class PlaceholderFragment : Fragment(), View.OnClickListener {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (arguments.getInt(ARG_SECTION_NUMBER) == 0) {
            inflateExpenseFragment(inflater)
        } else if (arguments.getInt(ARG_SECTION_NUMBER) == 1) {
            val fragmentMainView = inflater!!.inflate(R.layout.fragment_main, container, false)
            fragmentMainView?.setting_button?.setOnClickListener(this)
            return fragmentMainView
        } else if (arguments.getInt(ARG_SECTION_NUMBER) == 2) {
            inflateIncomeFragment(inflater)
        }
        return null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.sign_out_button -> showDialog()
            R.id.setting_button -> startActivity(Intent(context, SettingActivity::class.java))
        }
    }

    private fun inflateExpenseFragment(inflater: LayoutInflater?): View? =
        inflater?.inflate(R.layout.fragment_expense, container, false)

    private fun inflateIncomeFragment(inflater: LayoutInflater?): View? =
        inflater?.inflate(R.layout.fragment_income, container, false)

    private fun showDialog() {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder
            .setTitle(getString(R.string.confirmation))
            .setMessage(getString(R.string.confirmation_sign_out))
            .setPositiveButton(getString(R.string.yes), { _, _ ->
                auth.signOut()
            })
            .setNegativeButton(getString(R.string.no), { dialog, _ ->
                dialog.cancel()
            })
            .show()
    }

    companion object {

        private val ARG_SECTION_NUMBER = "section_number"

        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            val fragment = PlaceholderFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}