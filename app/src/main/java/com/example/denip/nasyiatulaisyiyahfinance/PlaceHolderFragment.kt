package com.example.denip.nasyiatulaisyiyahfinance

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_main.view.*

/**
 * Created by denip on 8/17/2017.
 */

class PlaceholderFragment : Fragment(), View.OnClickListener {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()



    override fun onClick(v: View?) {


    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (arguments.getInt(ARG_SECTION_NUMBER) == 1) {
            val fragmentIncomeView = inflater?.inflate(R.layout.fragment_expense, container, false)
            return fragmentIncomeView
        } else if (arguments.getInt(ARG_SECTION_NUMBER) == 2) {
            val fragmentMainView = inflater!!.inflate(R.layout.fragment_main, container, false)
            fragmentMainView?.button2_sign_out?.setOnClickListener {
                val dialogBuilder = AlertDialog.Builder(context)
                dialogBuilder
                    .setTitle(getString(R.string.confirmation))
                    .setMessage(getString(R.string.are_you_sure))
                    .setPositiveButton(getString(R.string.yes), { _, _ ->
                        auth.signOut()
                    })
                    .setNegativeButton(getString(R.string.no), { dialog, _ ->
                        dialog.cancel()
                    })
                    .show()
            }

            fragmentMainView.main_expense_button.setOnClickListener {

            }
            return fragmentMainView
        } else {
            val fragmentExpenseView = inflater!!.inflate(R.layout.fragment_income, container, false)
            return fragmentExpenseView
        }
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