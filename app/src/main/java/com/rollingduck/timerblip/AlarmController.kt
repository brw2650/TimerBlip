package com.rollingduck.timerblip

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.getSystemService
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

        val startTime = SettingsManager.getIntSetting(context, START_TIME, 8)
        val endTime = SettingsManager.getIntSetting(context, END_TIME, 22)

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

    private fun getNextStartTime(startTime: Int, endTime: Int): Calendar {

        val cal = Calendar.getInstance()
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        if (cal.get(Calendar.HOUR_OF_DAY) < startTime) {
            cal.set(Calendar.HOUR_OF_DAY, startTime)
            cal.set(Calendar.MINUTE, 0)
            return cal
        }

        if (cal.get(Calendar.HOUR_OF_DAY) >= endTime) {
            cal.set(Calendar.HOUR_OF_DAY, startTime)
            cal.add(Calendar.DAY_OF_MONTH, 1)
            cal.set(Calendar.MINUTE, 0)
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