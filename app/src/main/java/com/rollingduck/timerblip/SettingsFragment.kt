package com.rollingduck.timerblip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rollingduck.timerblip.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.updateUI()

        binding.buttonHome.setOnClickListener {
            findNavController().navigate(R.id.action_SettingsFragment_to_HomeFragment)
        }

        binding.buttonSetTime.setOnClickListener {
            findNavController().navigate(R.id.action_SettingsFragment_to_StartTimeFragment)
        }
    }

    override fun onResume() {
        super.onResume()

        this.updateUI()
    }

    private fun updateUI() {
        this.context?.let {
            val startTime = SettingsManager.getCalSetting(
                it,
                SettingsManager.START_TIME,
                SettingsManager.DEFAULT_START_TIME,
                SettingsManager.DEFAULT_MIN_TIME
            )
            binding.startTimeDisplay.text = getString(R.string.start_time_display, startTime);

            val endTime = SettingsManager.getCalSetting(
                it,
                SettingsManager.END_TIME,
                SettingsManager.DEFAULT_END_TIME,
                SettingsManager.DEFAULT_MIN_TIME
            )
            binding.endTimeDisplay.text = getString(R.string.end_time_display, endTime);

            val interval = SettingsManager.getIntSetting(
                it,
                SettingsManager.INTERVAL,
                SettingsManager.DEFAULT_INTERVAL
            )
            binding.intervalDisplay.text = getString(R.string.interval_display, interval)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}