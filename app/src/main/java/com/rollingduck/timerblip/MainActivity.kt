package com.rollingduck.timerblip

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rollingduck.timerblip.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cancelButton.setOnClickListener {
            AlarmController.cancelAlarm(this)
            updateUI()
        }

        binding.setButton.setOnClickListener {
            setAlarm()
            updateUI()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            showNotificationPermission()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showNotificationPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this, Manifest.permission.POST_NOTIFICATIONS
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            ) {
                Toast
                    .makeText(
                        this,
                        "Notification permission required for app to work",
                        Toast.LENGTH_LONG
                    )
                    .show()
            } else {
                val permissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
                ActivityCompat.requestPermissions(
                    this,
                    permissions,
                    1
                )
            }
        } else {
            Log.d("MainActivity", "Notification permission already granted")
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
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