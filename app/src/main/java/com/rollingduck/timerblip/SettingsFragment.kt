package com.rollingduck.timerblip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rollingduck.timerblip.databinding.FragmentSettingsBinding
import java.util.*

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
            val startTime = SettingsManager.getSetting(
                it,
                SettingsManager.START_TIME,
                SettingsManager.DEFAULT_START_TIME
            )
            val startTimeMin = SettingsManager.getSetting(
                it,
                SettingsManager.START_TIME_MIN,
                SettingsManager.DEFAULT_MIN_TIME
            )
            val startDate = this.createTime(startTime, startTimeMin)
            binding.startTimeDisplay.text = getString(R.string.start_time_display, startDate);

            val endTime = SettingsManager.getSetting(
                it,
                SettingsManager.END_TIME,
                SettingsManager.DEFAULT_END_TIME
            )
            val endTimeMin = SettingsManager.getSetting(
                it,
                SettingsManager.END_TIME_MIN,
                SettingsManager.DEFAULT_MIN_TIME
            )
            val endDate = this.createTime(endTime, endTimeMin)
            binding.endTimeDisplay.text = getString(R.string.end_time_display, endDate);
        }
    }

    private fun createTime(hour: Int, minute: Int): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        return cal.time
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}