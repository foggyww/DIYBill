package com.example.hustbill.config

import com.example.hustbill.utils.SP

object Config {

    var budget:Int = SP.getInt(SIGNAL.BUDGET,2000)
        set(value){
            SP.setInt(SIGNAL.BUDGET,value)
            field = value
        }

    private object SIGNAL{
        const val BUDGET = "budget"
    }
}