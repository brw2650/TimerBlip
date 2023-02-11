package com.rollingduck.timerblip

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rollingduck.timerblip.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.updateUI()
        binding.buttonToggleAlarm.setOnClickListener {
            this.toggleAlarm()
            this.updateUI()
        }

        binding.buttonSettings.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_SettingsFragment)
        }
    }

    private fun updateUI() {
        binding.textviewAlarmSet.text =
            if (AlarmController.isAlarmSet()) getString(R.string.alarm_set) else getString(R.string.alarm_not_set)
        binding.buttonToggleAlarm.text =
            if (AlarmController.isAlarmSet()) getString(R.string.cancel_alarm) else getString(R.string.set_alarm)
    }

    private fun toggleAlarm() {
        val context = this.requireContext()
        if (AlarmController.isAlarmSet()) {
            // Cancel
            AlarmController.cancelAlarm(context)
        } else {
            setAlarm(context)
        }
    }

    private fun setAlarm(context: Context) {
        AlarmController.setAlarm(context)

        val receiver = ComponentName(context, BootReceiver::class.java)

        this.activity?.packageManager?.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}