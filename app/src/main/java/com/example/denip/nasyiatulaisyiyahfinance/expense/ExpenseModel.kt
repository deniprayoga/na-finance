package com.example.denip.nasyiatulaisyiyahfinance.expense

/**
 * Created by denip on 9/18/2017.
 */

data class ExpenseModel(val addedByTreasure: String?,
                        val amount: Int?,
                        val category: String?,
                        val dateCreated: String?,
                        val expenseId: String?,
                        val note: String?,
                        val notePhotoUri: String?) {
    constructor() : this("", 0, "", "", "", "", "")
}