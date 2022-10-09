package com.rollingduck.timerblip

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.core.content.getSystemService

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AlarmReceiver", "Alarm triggered")
        vibrate(context)
    }

    private fun vibrate(context: Context?) {
        if (context == null) {
            Log.e("AlarmReceiver", "No context")
            return
        }

        val vibrator = context.getSystemService<Vibrator>()

        if (vibrator == null) {
            Log.e("VibrateWorker", "Cannot get vibrator")
            return
        }

        val effect = VibrationEffect.createOneShot(5000, 9)
        vibrator.vibrate(effect)
        Log.d("AlarmReceiver", "Vibration!")

        AlarmController.setAlarm(context)
    }
}