package com.rollingduck.timerblip

import android.content.Context
import android.content.Context.MODE_PRIVATE

object SettingsManager {

    const val START_TIME = "StartTime"
    const val END_TIME = "EndTime"

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