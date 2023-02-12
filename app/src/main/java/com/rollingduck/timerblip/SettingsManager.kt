package com.rollingduck.timerblip

import android.content.Context
import android.content.Context.MODE_PRIVATE
import java.util.*

object SettingsManager {

    const val START_TIME = "StartTimeHour"
    const val END_TIME = "EndTimeHour"

    const val DEFAULT_START_TIME = 8
    const val DEFAULT_END_TIME = 22
    const val DEFAULT_MIN_TIME = 0

    private const val timerSettings = "TimerSettings"

    fun getIntSetting(context: Context, settingName: String, default: Int): Int {
        val settings = context.getSharedPreferences(timerSettings, MODE_PRIVATE)
        return settings.getInt(settingName, default)
    }

    fun saveIntSetting(context: Context, settingName: String, value: Int) {
        val settings = context.getSharedPreferences(timerSettings, MODE_PRIVATE)
        val editor = settings.edit()
        editor.putInt(settingName, value)
        editor.apply()
    }

    fun getCalSetting(
        context: Context,
        settingName: String,
        defaultHour: Int,
        defaultMin: Int
    ): Calendar {
        val settings = context.getSharedPreferences(timerSettings, MODE_PRIVATE)
        val hour = settings.getInt(settingName + "_HOUR", defaultHour)
        val min = settings.getInt(settingName + "_MIN", defaultMin)

        return createTime(hour, min)
    }

    fun saveCalSetting(context: Context, settingName: String, value: Calendar) {
        val settings = context.getSharedPreferences(timerSettings, MODE_PRIVATE)
        val editor = settings.edit()
        editor.putInt(settingName + "_HOUR", value.get(Calendar.HOUR_OF_DAY))
        editor.putInt(settingName + "_MIN", value.get(Calendar.MINUTE))
        editor.apply()
    }

    private fun createTime(hour: Int, minute: Int): Calendar {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        return cal
    }
}