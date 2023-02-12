package com.rollingduck.timerblip

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.getSystemService
import com.rollingduck.timerblip.SettingsManager.DEFAULT_END_TIME
import com.rollingduck.timerblip.SettingsManager.DEFAULT_MIN_TIME
import com.rollingduck.timerblip.SettingsManager.DEFAULT_START_TIME
import com.rollingduck.timerblip.SettingsManager.END_TIME
import com.rollingduck.timerblip.SettingsManager.START_TIME
import java.util.*
import kotlin.math.floor

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

        val startTime =
            SettingsManager.getCalSetting(context, START_TIME, DEFAULT_START_TIME, DEFAULT_MIN_TIME)
        val endTime =
            SettingsManager.getCalSetting(context, END_TIME, DEFAULT_END_TIME, DEFAULT_MIN_TIME)

        val cal = getNextStartTime(startTime, endTime)
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
        return getNextStartTime(startTime, endTime)
    }

    private fun getNextStartTime(startTime: Calendar, endTime: Calendar): Calendar {

        var cal = Calendar.getInstance()
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        if (cal < startTime) {
            cal = startTime.clone() as Calendar
            return cal
        }

        if (cal >= endTime) {
            cal = startTime.clone() as Calendar
            cal.add(Calendar.DAY_OF_MONTH, 1)
            return cal
        }

        val interval = floor((cal.get(Calendar.MINUTE) / 30).toDouble())
        if (interval.toInt() == 0) {
            cal.set(Calendar.MINUTE, 30)
        } else {
            cal.set(Calendar.MINUTE, 0)
            cal.add(Calendar.HOUR, 1)
        }

        return cal
    }
}