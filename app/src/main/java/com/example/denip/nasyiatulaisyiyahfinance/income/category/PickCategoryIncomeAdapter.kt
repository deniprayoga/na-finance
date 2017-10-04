package com.example.denip.nasyiatulaisyiyahfinance.income.category

import android.content.Context
import android.preference.PreferenceManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.denip.nasyiatulaisyiyahfinance.R

/**
 * Created by denip on 10/4/2017.
 */

class PickCategoryIncomeAdapter(private var context: Context, categories: ArrayList<CategoryIncomeModel>) :
    RecyclerView.Adapter<PickCategoryIncomeAdapter.CustomViewHolder>() {

    private var categories: ArrayList<CategoryIncomeModel> = arrayListOf()

    init {
        this.categories = categories
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.pick_category_list_row_view,
            parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        val category = categories[position]
        holder!!.categoryNumber.text = category.categoryNumber
        holder.categoryName.text = category.categoryName.toString()
        holder.categoryFirstNumber.text = category.firstNumber.toString()
        holder.categorySecondNumber.text = category.secondNumber.toString()
        holder.categoryThirdNumber.text = category.thirdNumber.toString()
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryNumber = view.findViewById(R.id.pick_category_list_row_view_category_number) as TextView
        val categoryName = view.findViewById(R.id.pick_category_list_row_view_category_name) as TextView
        val categoryFirstNumber = view.findViewById(R.id.pick_category_first_number) as TextView
        val categorySecondNumber = view.findViewById(R.id.pick_category_second_number) as TextView
        val categoryThirdNumber = view.findViewById(R.id.pick_category_third_number) as TextView

        init {
            view.setOnClickListener { _ ->
                val selectedCategory = categories[adapterPosition]
                notifyDataSetChanged()

                val categoryName = selectedCategory.categoryName.toString()
                val categoryNumber = selectedCategory.categoryNumber.toString()

                val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                val editor = prefs.edit().apply {
                    putString(context.getString(R.string.CATEGORY_NUMBER_INCOME), categoryNumber)
                    putString(context.getString(R.string.CATEGORY_NAME_INCOME), categoryName)
                    commit()
                }

                (context as PickCategoryIncomeActivity).finish()
            }
        }
    }
}