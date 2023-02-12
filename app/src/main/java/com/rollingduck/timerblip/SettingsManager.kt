package com.rollingduck.timerblip

import android.content.Context
import android.content.Context.MODE_PRIVATE

object SettingsManager {

    const val START_TIME = "StartTimeHour"
    const val START_TIME_MIN = "StartTimeMin"

    const val END_TIME = "EndTimeHour"
    const val END_TIME_MIN = "EndTimeMin"

    const val DEFAULT_START_TIME = 8
    const val DEFAULT_END_TIME = 8
    const val DEFAULT_MIN_TIME = 0

    private const val timerSettings = "TimerSettings"

    fun getSetting(context: Context, settingName: String, default: Int): Int {
        val settings = context.getSharedPreferences(timerSettings, MODE_PRIVATE)
        return settings.getInt(settingName, default)
    }

    fun saveSetting(context: Context, settingName: String, value: Int) {
        val settings = context.getSharedPreferences(timerSettings, MODE_PRIVATE)
        val editor = settings.edit()
        editor.putInt(settingName, value)
        editor.apply()
    }
}