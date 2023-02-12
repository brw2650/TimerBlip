package com.rollingduck.timerblip

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rollingduck.timerblip.databinding.FragmentEndTimeBinding
import java.util.*

class EndTimeFragment : Fragment() {

    private var _binding: FragmentEndTimeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEndTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonNext.setOnClickListener {
            this.context?.let {
                val date = Calendar.getInstance()
                date.set(Calendar.HOUR_OF_DAY, binding.timePicker.hour)
                date.set(Calendar.MINUTE, binding.timePicker.minute)
                SettingsManager.saveCalSetting(
                    it,
                    SettingsManager.END_TIME,
                    date
                )
            }

            val toast = Toast.makeText(this.context, "Saved", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.BOTTOM, 0, 0)
            toast.show()

            if (AlarmController.isAlarmSet()) {
                Log.d("EndTimeFragment", "Resetting alarm to pick up settings change")
                this.context?.let {
                    AlarmController.cancelAlarm(it)
                    AlarmController.setAlarm(it)
                }
            }

            findNavController().navigate(R.id.action_EndTimeFragment_to_SettingsFragment)
        }
    }

    override fun onResume() {
        super.onResume()

        this.updateUI()
    }

    private fun updateUI() {
        this.context?.let {
            val endTime = SettingsManager.getCalSetting(
                it,
                SettingsManager.END_TIME,
                SettingsManager.DEFAULT_END_TIME,
                SettingsManager.DEFAULT_MIN_TIME
            )
            binding.timePicker.hour = endTime.get(Calendar.HOUR_OF_DAY)
            binding.timePicker.minute = endTime.get(Calendar.MINUTE)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}