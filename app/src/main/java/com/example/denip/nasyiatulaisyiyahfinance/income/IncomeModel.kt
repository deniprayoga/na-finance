package com.example.denip.nasyiatulaisyiyahfinance.income

/**
 * Created by denip on 9/18/2017.
 */

class IncomeModel(val incomeId: String?,
                  val addedByTreasurer: String?,
                  val amount: Long?,
                  val category: String?,
                  val dateCreated: String?,
                  val note: String?,
                  val addedByTreasurerInitial: String?,
                  val addedByTreasurerUid: String?,
                  val categoryId: String?) {
    constructor() : this("", "", 0, "", "", "", "", "", "")
}