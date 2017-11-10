package com.example.denip.nasyiatulaisyiyahfinance.income.category

import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.category_list_row_view.view.*

/**
 * Created by denip on 9/28/2017.
 */

class CategoryIncomeListAdapter(context: Context, categories: ArrayList<CategoryIncomeModel>) :
    RecyclerView.Adapter<CategoryIncomeListAdapter.CustomViewHolder>() {

    private var context: Context = context
    private var categories: ArrayList<CategoryIncomeModel> = arrayListOf()

    init {
        this.categories = categories
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.category_list_row_view,
            parent, false)
        view.category_list_row_view_category_name?.isSelected = true
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        val category = categories[position]
        holder!!.categoryNumber.text = category.categoryNumber?.replace("-", " - ")
        holder.categoryName.text = category.categoryName
        holder.categoryFirstNumber.text = category.firstNumber.toString()
        holder.categorySecondNumber.text = category.secondNumber.toString()
        holder.categoryThirdNumber.text = category.thirdNumber.toString()
    }

    override fun getItemCount(): Int = categories.size

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryNumber = view.findViewById(R.id.category_list_row_view_category_number) as TextView
        val categoryName = view.findViewById(R.id.category_list_row_view_category_name) as TextView
        val categoryFirstNumber = view.findViewById(R.id.category_first_number) as TextView
        val categorySecondNumber = view.findViewById(R.id.category_second_number) as TextView
        val categoryThirdNumber = view.findViewById(R.id.category_third_number) as TextView

        init {
            view.setOnClickListener { v ->
                val category = categories[adapterPosition]
                notifyDataSetChanged()
                val intent = Intent(v?.context, CategoryIncomeDetailActivity::class.java)
                intent.putExtra(v?.context?.getString(R.string.CATEGORY_ID_INCOME), category.categoryId)
                intent.putExtra(v?.context?.getString(R.string.CATEGORY_NUMBER_INCOME), category.categoryNumber)
                intent.putExtra(v?.context?.getString(R.string.CATEGORY_NAME_INCOME), category.categoryName)
                intent.putExtra(context?.getString(R.string.CATEGORY_FIRST_NUMBER_INCOME), category.firstNumber.toString())
                intent.putExtra(v?.context?.getString(R.string.CATEGORY_SECOND_NUMBER_INCOME), category.secondNumber.toString())
                intent.putExtra(v?.context?.getString(R.string.CATEGORY_THIRD_NUMBER_INCOME), category.thirdNumber.toString())

                Log.d("hunter_categoryId", category.categoryId)
                Log.d("hunter_categoryNumber", category.categoryNumber)
                Log.d("hunter_categoryName", category.categoryName)
                Log.d("hunter_catFirstNumSend", category.firstNumber.toString())
                Log.d("hunter_categorySeconNum", category.secondNumber.toString())
                Log.d("hunter_categoryThirdNum", category.thirdNumber.toString())
                v?.context?.startActivity(intent)
            }
            view.setOnLongClickListener { v ->
                val category = categories[adapterPosition]
                val dialogBuilder = AlertDialog.Builder(context)
                dialogBuilder
                    .setTitle(context.getString(R.string.confirmation))
                    .setMessage(context.getString(R.string.delete_category_message))
                    .setPositiveButton(context.getString(R.string.yes), { dialog, which ->
                        val dbDeleteCategoryRef = FirebaseDatabase
                            .getInstance()
                            .getReference("categories/income")
                            .child(category.categoryId)
                        dbDeleteCategoryRef.removeValue()
                        showDeletedSuccessfully()
                        notifyItemRemoved(adapterPosition)
                    })
                    .setNegativeButton(context.getString(R.string.no), { dialog, which ->
                        dialog.dismiss()
                    })
                val dialog = dialogBuilder.create()
                dialog.show()
                true
            }
        }

    }

    private fun showDeletedSuccessfully() {
        Toast.makeText(context, context.getString(R.string.category_deleted), Toast.LENGTH_SHORT).show()
    }

}