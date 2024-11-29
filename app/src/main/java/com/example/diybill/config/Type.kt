package com.example.diybill.config

/**
 * 支出类
 */
enum class OutlayType(override val cnName:String):Type{
    Eating("吃饭"),
    Cloth("衣物"),
    Transfer("个人转账"),
    Education("教育"),
    Traffic("交通"),
    Medical("医疗"),
    Amusement("娱乐"),
    Other("其他支出");
    override val isOutlay: Boolean = true
}

interface Type{
    val cnName:String //种类的名称
    val isOutlay:Boolean //是否为支出账单
}

/**
 * 收入类
 */
enum class IncomeType(override val cnName: String):Type{
    Work("工资"),
    Dividends("分红"),
    Other("其他收入");

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

val Type.toTypeList:List<Type>
    get() {
        return if(this is OutlayType){
            OutlayType.entries.toList()
        }else{
            IncomeType.entries.toList()
        }
    }

val String.toOutlayType:OutlayType
    get() {
        for (type in OutlayType.entries){
            if(type.cnName==this){
                return type
            }
        }
        throw Throwable("账单的type不存在")
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