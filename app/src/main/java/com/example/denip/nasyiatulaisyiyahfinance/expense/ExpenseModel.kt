package com.example.denip.nasyiatulaisyiyahfinance.expense

/**
 * Created by denip on 9/18/2017.
 */

class ExpenseModel(val addedByTreasurer: String?,
                   val treasurerUid: String?,
                   val amount: String?,
                   val category: String?,
                   val dateCreated: String?,
                   val expenseId: String?,
                   val note: String?,
                   val notePhotoUri: String?,
                   val categoryId: String?) {
    constructor() : this("","", "", "", "", "", "", "", "")
    constructor(addedByTreasurer: String?) : this(addedByTreasurer, "", "", "", "", "", "", "", "")
}