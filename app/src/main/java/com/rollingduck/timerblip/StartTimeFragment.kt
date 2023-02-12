package com.rollingduck.timerblip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rollingduck.timerblip.databinding.FragmentStartTimeBinding

class StartTimeFragment : Fragment() {

    private var _binding: FragmentStartTimeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStartTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.updateUI()

        binding.buttonNext.setOnClickListener {
            this.context?.let {
                SettingsManager.saveSetting(
                    it,
                    SettingsManager.START_TIME,
                    binding.timePicker.hour
                )
                SettingsManager.saveSetting(
                    it,
                    SettingsManager.START_TIME_MIN,
                    binding.timePicker.minute
                )
            }
            findNavController().navigate(R.id.action_StartTimeFragment_to_EndTimeFragment)
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
            binding.timePicker.hour = startTime
            val startTimeMin = SettingsManager.getSetting(
                it,
                SettingsManager.START_TIME_MIN,
                SettingsManager.DEFAULT_MIN_TIME
            )
            binding.timePicker.minute = startTimeMin
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}