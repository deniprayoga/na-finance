package com.example.denip.nasyiatulaisyiyahfinance.user

/**
 * Created by denip on 9/17/2017.
 */

data class UserModel(val userId: String?,
                     val fullName: String?,
                     val phoneNumber: String?,
                     val location: String?,
                     val email: String?,
                     val initial: String?,
                     val position: String?) {
    constructor() : this("", "", "", "", "", "", "")
}