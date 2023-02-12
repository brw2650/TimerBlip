package com.rollingduck.timerblip

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.rollingduck.timerblip.databinding.ActivityMainBinding

class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            showNotificationPermission()
        } else {
            with(NotificationManagerCompat.from(this)) {
                val notificationsEnabled = areNotificationsEnabled()
                if (!notificationsEnabled) {
                    showNotificationWarning()
                }
            }
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
                showNotificationWarning()
            } else {
                Log.d("MainActivity", "Requesting notification permission")
                val permissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
                ActivityCompat.requestPermissions(
                    this,
                    permissions,
                    0
                )
            }
        } else {
            Log.d("MainActivity", "Notification permission already granted")
        }
    }

    private fun showNotificationWarning() {
        Toast
            .makeText(
                this,
                getString(R.string.permission_warning),
                Toast.LENGTH_LONG
            )
            .show()
    }

    override fun onResume() {
        super.onResume()
    }
}