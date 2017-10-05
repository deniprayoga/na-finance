package com.example.denip.nasyiatulaisyiyahfinance.expense.category

import android.content.Context
import android.preference.PreferenceManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.denip.nasyiatulaisyiyahfinance.R

/**
 * Created by denip on 10/1/2017.
 */

class PickCategoryExpenseAdapter(private var context: Context, categories: ArrayList<CategoryExpenseModel>) :
    RecyclerView.Adapter<PickCategoryExpenseAdapter.CustomViewHolder>() {

    private var categories: ArrayList<CategoryExpenseModel> = arrayListOf()
    private val HUNTR = "huntr_pckctgrxpnsadptr"


    init {
        this.categories = categories
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.pick_category_list_row_view,
            parent, false)
        Log.d(HUNTR, "In onCreateView()")
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        val category = categories[position]
        holder!!.categoryNumber.text = category.categoryNumber
        holder.categoryName.text = category.categoryName.toString()
        holder.categoryFirstNumber.text = category.firstNumber.toString()
        holder.categorySecondNumber.text = category.secondNumber.toString()
        holder.categoryThirdNumber.text = category.thirdNumber.toString()
        Log.d(HUNTR, "In onBindViewHolder()")
    }

    override fun getItemCount(): Int = categories.size

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val categoryNumber = view.findViewById(R.id.pick_category_list_row_view_category_number) as TextView
        val categoryName = view.findViewById(R.id.pick_category_list_row_view_category_name) as TextView
        val categoryFirstNumber = view.findViewById(R.id.pick_category_first_number) as TextView
        val categorySecondNumber = view.findViewById(R.id.pick_category_second_number) as TextView
        val categoryThirdNumber = view.findViewById(R.id.pick_category_third_number) as TextView

        init {
            view.setOnClickListener { v ->
                val selectedCategory = categories[adapterPosition]
                notifyDataSetChanged()

                val categoryName = selectedCategory.categoryName.toString()
                val categoryNumber = selectedCategory.categoryNumber.toString()

                val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                prefs.edit().apply {
                    putString(context.getString(R.string.CATEGORY_NUMBER_EXPENSE), categoryNumber)
                    putString(context.getString(R.string.CATEGORY_NAME_EXPENSE), categoryName)
                    commit()
                }

                (context as PickCategoryExpenseActivity).finish()

                Log.d(HUNTR, "" + selectedCategory.categoryNumber)
                Log.d(HUNTR, "" + selectedCategory.categoryName)
                Log.d(HUNTR, "" + selectedCategory.firstNumber)
                Log.d(HUNTR, "" + selectedCategory.secondNumber)
                Log.d(HUNTR, "" + selectedCategory.thirdNumber)
            }
        }
    }
}