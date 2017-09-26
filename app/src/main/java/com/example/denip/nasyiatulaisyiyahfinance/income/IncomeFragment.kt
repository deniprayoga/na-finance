package com.example.denip.nasyiatulaisyiyahfinance.income

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_income.*
import kotlinx.android.synthetic.main.fragment_income.view.*


class IncomeFragment : Fragment(), View.OnClickListener {

    companion object {

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        private var mParam1: String? = null
        private var mParam2: String? = null
        private val dbIncomeRef = FirebaseDatabase.getInstance().getReference("incomes")
        private val incomes = ArrayList<IncomeModel>()

        fun newInstance(param1: String, param2: String): IncomeFragment {
            val fragment = IncomeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val fragmentIncomeView = inflater!!.inflate(R.layout.fragment_income, container, false)

        fragmentIncomeView.add_income_button.setOnClickListener(this)
        fragmentIncomeView.export_income_imageButton.setOnClickListener(this)
        return fragmentIncomeView
    }

    override fun onStart() {
        super.onStart()
        dbIncomeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                incomes.clear()

                dataSnapshot!!.children
                    .map { it.getValue(IncomeModel::class.java) }
                    .forEach { incomes.add(it) }

                val incomeAdapter = IncomeListAdapter(context, incomes)
                recycler_view_income_list?.adapter = incomeAdapter

            }

            override fun onCancelled(databaseError: DatabaseError?) {

            }
        })
        recycler_view_income_list?.layoutManager = LinearLayoutManager(context)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add_income_button -> {
                startActivity(Intent(context, AddIncomeActivity::class.java))
            }
            R.id.export_income_imageButton -> {
                val dialogBuilder = AlertDialog.Builder(context)
                dialogBuilder
                    .setTitle(R.string.confirmation)
                    .setMessage(R.string.confirmation_export_income)
                    .setPositiveButton(R.string.yes, { dialog, _ ->
                        Toast.makeText(context, "Make your export income action.", Toast.LENGTH_SHORT).show()
                    })
                    .setNegativeButton(R.string.no, { dialog, _ ->
                        dialog.cancel()
                    })

                    .show()
            }
        }
    }

    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onAttachFragment(childFragment: Fragment?) {
        super.onAttachFragment(childFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
