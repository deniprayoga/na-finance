package com.example.denip.nasyiatulaisyiyahfinance.expense

/**
 * Created by denip on 9/18/2017.
 */

class ExpenseModel(val addedByTreasurer: String?,
                   val addedByTreasurerUid: String?,
                   val amount: String?,
                   val category: String?,
                   val dateCreated: String?,
                   val expenseId: String?,
                   val note: String?,
                   val addedByTreasurerInitial: String?,
                   val categoryId: String?) {
    constructor() : this("","", "", "", "", "", "", "", "")
    constructor(addedByTreasurer: String?) : this(addedByTreasurer, "", "", "", "", "", "", "", "")
}