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
import com.rollingduck.timerblip.SettingsManager.DEFAULT_INTERVAL
import com.rollingduck.timerblip.SettingsManager.INTERVAL
import com.rollingduck.timerblip.databinding.FragmentIntervalBinding

class IntervalFragment : Fragment() {

    private var _binding: FragmentIntervalBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentIntervalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonNext.setOnClickListener {
            this.context?.let {
                val interval = binding.numberPicker.value
                SettingsManager.saveIntSetting(it, INTERVAL, interval)
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

            findNavController().navigate(R.id.action_IntervalFragment_to_SettingsFragment)
        }

        val min = 0
        val max = 60
        val displayRange = (min..max).map {
            it.toString()
        }.toTypedArray()

        binding.numberPicker.minValue = min
        binding.numberPicker.maxValue = max
        binding.numberPicker.wrapSelectorWheel = false
        binding.numberPicker.displayedValues = displayRange
        this.updateUI()
    }

    override fun onResume() {
        super.onResume()

        this.updateUI()
    }

    private fun updateUI() {
        this.context?.let {
            val interval = SettingsManager.getIntSetting(it, INTERVAL, DEFAULT_INTERVAL)
            binding.numberPicker.value = interval
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}