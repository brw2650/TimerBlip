package com.rollingduck.timerblip

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.getSystemService
import com.rollingduck.timerblip.SettingsManager.DEFAULT_END_TIME
import com.rollingduck.timerblip.SettingsManager.DEFAULT_INTERVAL
import com.rollingduck.timerblip.SettingsManager.DEFAULT_MIN_TIME
import com.rollingduck.timerblip.SettingsManager.DEFAULT_START_TIME
import com.rollingduck.timerblip.SettingsManager.END_TIME
import com.rollingduck.timerblip.SettingsManager.INTERVAL
import com.rollingduck.timerblip.SettingsManager.START_TIME
import java.util.*

object AlarmController {

    private var pendingIntent: PendingIntent? = null

    fun isAlarmSet(): Boolean {
        return pendingIntent != null
    }

    fun setAlarm(context: Context?) {

        if (context == null) {
            Log.e("AlarmController", "No Context")
            return
        }

        Log.d("AlarmController", "Setting alarm")

        val alarmManager = context.getSystemService<AlarmManager>()

        if (alarmManager == null) {
            Log.e("AlarmController", "No alarm manager")
            return
        }

        val intent = Intent(context, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val cal = getNextAlarm(context)
        Log.d("AlarmController", "Next alarm: ${cal.time}")

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            cal.timeInMillis,
            pendingIntent
        )
    }

    fun cancelAlarm(context: Context) {
        pendingIntent?.let {
            Log.d("AlarmController", "Cancelling alarm")
            val alarmManager = context.getSystemService<AlarmManager>()
            alarmManager?.cancel(it)
        }
        pendingIntent = null
    }

    fun getNextAlarm(context: Context): Calendar {
        val startTime =
            SettingsManager.getCalSetting(context, START_TIME, DEFAULT_START_TIME, DEFAULT_MIN_TIME)
        val endTime =
            SettingsManager.getCalSetting(context, END_TIME, DEFAULT_END_TIME, DEFAULT_MIN_TIME)
        val interval =
            SettingsManager.getIntSetting(context, INTERVAL, DEFAULT_INTERVAL)
        return getNextStartTime(startTime, endTime, interval)
    }

    private fun getNextStartTime(startTime: Calendar, endTime: Calendar, interval: Int): Calendar {

        val now = Calendar.getInstance()

        if (now < startTime) {
            return startTime.clone() as Calendar
        }

        val nextStart = startTime.clone() as Calendar
        while (nextStart < now) {
            nextStart.add(Calendar.MINUTE, interval)
        }

        if (nextStart > endTime) {
            val tomorrow = startTime.clone() as Calendar
            tomorrow.add(Calendar.DAY_OF_MONTH, 1)
            return tomorrow
        }

        return nextStart
    }
}