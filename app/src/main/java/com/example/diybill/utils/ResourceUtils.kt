package com.example.diybill.utils

import android.content.res.Resources
import com.example.diybill.App

val resource: Resources = App.CONTEXT.resources

fun string(id: Int, vararg formatArgs: Any) = resource.getString(id, *formatArgs)