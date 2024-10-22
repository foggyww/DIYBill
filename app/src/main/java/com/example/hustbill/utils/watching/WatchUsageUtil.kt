package com.example.hustbill.utils.watching

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context

object WatchUsageUtil {
    data class UsageState(
        val packageName: String,
        val className : String
    )

    fun getUsageStats(context: Context):List<UsageState>{
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 1000 // 查询过去一秒内的使用情况
        val list = mutableListOf<UsageState>()
        val usageEvents = usageStatsManager.queryEvents(startTime, endTime)
        while (usageEvents.hasNextEvent()) {
            val event = UsageEvents.Event()
            usageEvents.getNextEvent(event)

            if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                val packageName = event.packageName
                val className = event.className
                list.add(UsageState(packageName, className))
            }
        }
        return list.toList()
    }
}
