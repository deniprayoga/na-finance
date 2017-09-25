package com.example.denip.nasyiatulaisyiyahfinance.income

/**
 * Created by denip on 9/18/2017.
 */

data class IncomeModel(val incomeId: String?,
                       val addedByTreasure: String?,
                       val amount: Int?,
                       val category: String?,
                       val dateCreated: String?,
                       val note: String?,
                       val notePhotoUri: String?) {
    constructor() : this("", "", 0, "", "", "", "")
}