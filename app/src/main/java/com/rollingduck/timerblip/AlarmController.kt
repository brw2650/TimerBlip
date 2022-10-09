package com.rollingduck.timerblip

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.*
import kotlin.math.floor

object AlarmController {

    private var pendingIntent: PendingIntent? = null
    private const val startTime = 8
    private const val endTime = 22

    fun isAlarmSet(): Boolean {
        return pendingIntent != null
    }

    fun setAlarm(context: Context?) {

        if (context == null) {
            Log.e("AlarmController", "No Context")
            return
        }

        Log.d("AlarmController", "Setting alarm")

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
//        intent.action = MyIntentService.ACTION_SEND_TEST_MESSAGE
//        intent.putExtra(MyIntentService.EXTRA_MESSAGE, message)

        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)

        val cal = getNextStartTime()
        Log.d("AlarmController", "Next alarm: ${cal.time}")

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            cal.timeInMillis,
            pendingIntent
        )
    }

    fun cancelAlarm(context: Context) {
        pendingIntent?.let {
            Log.d("AlarmController", "Cancelling alarm")
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(it)
        }
        pendingIntent = null
    }

    private fun getNextStartTime(): Calendar {
        val cal = Calendar.getInstance()
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

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

        return cal;
    }
}