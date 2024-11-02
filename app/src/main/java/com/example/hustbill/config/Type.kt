package com.example.hustbill.config

enum class OutlayType(override val cnName:String):Type{
    Eating("吃饭"),
    Cloth("衣物"),
    Transfer("个人转账"),
    Education("教育"),
    Traffic("交通"),
    Medical("医疗"),
    Amusement("娱乐"),
    Other("其他");
    override val isOutlay: Boolean = true
}

interface Type{
    val cnName:String
    val isOutlay:Boolean
}

enum class IncomeType(override val cnName: String):Type{
    Work("工资"),
    Dividends("分红"),
    Other("其他");

    override val isOutlay: Boolean = false
}

val Type.toStringList:List<String>
    get() {
        return if(this is OutlayType){
            OutlayType.entries.map {
                it.cnName
            }
        }else{
            IncomeType.entries.map {
                it.cnName
            }
        }
    }

val String.toOutlayType:OutlayType?
    get() {
        for (type in OutlayType.entries){
            if(type.cnName==this){
                return type
            }
        }
        return null
    }

val String.toIncomeType:IncomeType?
    get() {
        for (type in IncomeType.entries){
            if(type.cnName==this){
                return type
            }
        }
        return null
    }