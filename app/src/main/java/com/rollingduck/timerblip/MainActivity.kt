package com.rollingduck.timerblip

import android.app.Activity
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import com.rollingduck.timerblip.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cancelButton.setOnClickListener(View.OnClickListener {
            AlarmController.cancelAlarm(this)
            updateUI()
        })

        binding.setButton.setOnClickListener(View.OnClickListener {
            setAlarm()
            updateUI()
        })
    }

    override fun onResume() {
        super.onResume()
        updateUI();
    }

    private fun updateUI() {
        if (AlarmController.isAlarmSet()) {
            binding.alarmSet.visibility = View.VISIBLE
            binding.alarmNotSet.visibility = View.INVISIBLE
            binding.cancelButton.visibility = View.VISIBLE
            binding.setButton.visibility = View.INVISIBLE
        } else {
            binding.alarmSet.visibility = View.INVISIBLE
            binding.alarmNotSet.visibility = View.VISIBLE
            binding.cancelButton.visibility = View.INVISIBLE
            binding.setButton.visibility = View.VISIBLE
        }
    }

    private fun setAlarm() {
        AlarmController.setAlarm(this)

        val receiver = ComponentName(this, BootReceiver::class.java)

        this.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}