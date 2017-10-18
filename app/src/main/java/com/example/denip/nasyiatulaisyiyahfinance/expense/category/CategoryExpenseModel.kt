package com.example.denip.nasyiatulaisyiyahfinance.expense.category

/**
 * Created by denip on 9/19/2017.
 */

class CategoryExpenseModel(var categoryId: String?,
                           var firstNumber: Int?,
                           var secondNumber: Int?,
                           var thirdNumber: Int?,
                           var categoryNumber: String?,
                           var categoryName: String?) {
    constructor() : this("", 0, 0, 0, "", "")
}