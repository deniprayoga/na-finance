package com.example.denip.nasyiatulaisyiyahfinance.login.model

/**
 * Created by denip on 8/3/2017.
 */

data class PengurusHarian(val uid: String?,
                          val name: String?,
                          val email: String?,
                          val password: String?) {
    companion object {
        const val PENGURUS_HARIAN = "PENGURUS_HARIAN"
    }
}