package com.example.denip.nasyiatulaisyiyahfinance.income.category

/**
 * Created by denip on 9/28/2017.
 */

data class CategoryIncomeModel(val categoryId: String?,
                               val firstNumber: Int?,
                               val secondNumber: Int?,
                               val thirdNumber: Int?,
                               val categoryNumber: String?,
                               val categoryName: String?) {
    constructor() : this("", 0, 0, 0, "", "")
}