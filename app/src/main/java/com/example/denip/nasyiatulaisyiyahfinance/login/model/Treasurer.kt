package com.example.denip.nasyiatulaisyiyahfinance.login.model

/**
 * Created by denip on 8/3/2017.
 */

data class Treasurer(val uid: String?,
                     val name: String?,
                     val email: String?,
                     val password: String?) {

    companion object {
        const val BENDAHARA_UMUM = "BENDAHARA_UMUM"
        const val BENDAHARA_1 = "BENDAHARA_1"
        const val BENDAHARA_2 = "BENDAHARA_2"
        const val BENDAHARA_3 = "BENDAHARA_3"
    }


}