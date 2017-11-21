package com.example.denip.nasyiatulaisyiyahfinance.income

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_income.*
import kotlinx.android.synthetic.main.fragment_income.view.*


class IncomeFragment : Fragment(), View.OnClickListener {

    companion object {
        private val auth = FirebaseAuth.getInstance()
        private val HUNTR = "huntr_IncomeFragment"
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        private var mParam1: String? = null
        private var mParam2: String? = null
        private val dbIncomeRef = FirebaseDatabase.getInstance().getReference("incomes")
        private val incomes = ArrayList<IncomeModel>()
        private lateinit var incomeAdapter: IncomeListAdapter

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

                incomeAdapter = IncomeListAdapter(activity?.applicationContext, incomes)
                recycler_view_income_list?.adapter = incomeAdapter

            }

            override fun onCancelled(databaseError: DatabaseError?) {

            }
        })
        recycler_view_income_list?.layoutManager = LinearLayoutManager(context)
    }

    fun showIncomeAll() {
        dbIncomeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                incomes.clear()

                dataSnapshot!!.children
                    .map { it.getValue(IncomeModel::class.java) }
                    .forEach { incomes.add(it) }

                incomeAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError?) {

            }
        })
    }

    fun showIncomeOnlyMe() {
        dbIncomeRef
            .orderByChild("addedByTreasurerUid")
            .equalTo(auth.currentUser?.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    incomes.clear()

                    dataSnapshot!!.children
                        .map { it.getValue(IncomeModel::class.java) }
                        .forEach { incomes.add(it) }

                    incomeAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(databaseError: DatabaseError?) {

                }
            })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add_income_button -> {
                startActivity(Intent(context, AddIncomeActivity::class.java))
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
        Log.d(HUNTR, "In the onDetach() event")
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    override fun onPause() {
        Log.d(HUNTR, "In the onPause() event")
        super.onPause()
    }

    override fun onResume() {
        Log.d(HUNTR, "In the onResume() event")
        super.onResume()
    }

    override fun onAttachFragment(childFragment: Fragment?) {
        Log.d(HUNTR, "In the onAttachFragment() event")
        super.onAttachFragment(childFragment)
    }

    override fun onDestroyView() {
        Log.d(HUNTR, "In the onDestroyView() event")
        super.onDestroyView()
    }

    override fun onStop() {
        Log.d(HUNTR, "In the onStop() event")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(HUNTR, "In the onDestroy() event")
        super.onDestroy()
    }
}
