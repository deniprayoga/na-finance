package com.example.denip.nasyiatulaisyiyahfinance.category

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.denip.nasyiatulaisyiyahfinance.R

/**
 * Created by denip on 9/26/2017.
 */

class CategoryListAdapter(context: Context, categories: ArrayList<CategoryModel>) :
    RecyclerView.Adapter<CategoryListAdapter.CustomViewHolder>() {

    companion object {

    }

    private var categories: ArrayList<CategoryModel> = arrayListOf()

    init {
        this.categories = categories
    }

    override fun getItemCount(): Int = categories.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.category_list_row_view,
            parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        val category = categories[position]
        holder!!.categoryNumber.text = category.categoryNumber
        holder.categoryName.text = category.categoryName
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryNumber = view.findViewById(R.id.category_list_row_view_category_number) as TextView
        val categoryName = view.findViewById(R.id.category_list_row_view_category_name) as TextView

        init {
            view.setOnClickListener { v ->

            }
        }
    }
}