package com.example.denip.nasyiatulaisyiyahfinance.category

/**
 * Created by denip on 9/19/2017.
 */

data class CategoryModel(val categoryId: String?,
                         val categoryNumber: String?,
                         val categoryName: String?) {
    constructor() : this("", "", "")
}