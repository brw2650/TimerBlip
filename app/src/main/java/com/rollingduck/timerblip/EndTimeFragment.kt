package com.rollingduck.timerblip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    ): View {

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

            findNavController().navigate(R.id.action_EndTimeFragment_to_IntervalFragment)
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