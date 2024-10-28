package com.example.hustbill.config

enum class BillType(val cnName:String){
    eating("吃饭")
}

val String.toBillType:BillType?
    get() {
        for (type in BillType.entries){
            if(type.cnName==this){
                return type
            }
        }
        return null
    }