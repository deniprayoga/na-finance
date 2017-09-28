package com.example.denip.nasyiatulaisyiyahfinance.expense.category

/**
 * Created by denip on 9/19/2017.
 */

data class CategoryExpenseModel(val categoryId: String?,
                                val firstNumber: Int?,
                                val secondNumber: Int?,
                                val thirdNumber: Int?,
                                val categoryNumber: String?,
                                val categoryName: String?) {
    constructor() : this("", 0, 0, 0, "", "")
}