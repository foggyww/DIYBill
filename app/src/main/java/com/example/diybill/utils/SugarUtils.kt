package com.example.diybill.utils

import com.blankj.utilcode.util.EncryptUtils

fun md5(raw: ByteArray): String {
    return EncryptUtils.encryptMD5ToString(raw)
}