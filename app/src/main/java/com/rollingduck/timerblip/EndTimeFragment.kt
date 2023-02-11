package com.rollingduck.timerblip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rollingduck.timerblip.databinding.FragmentEndTimeBinding

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
                SettingsManager.saveSetting(
                    it,
                    SettingsManager.END_TIME,
                    binding.timePicker.hour
                )
            }
            Toast.makeText(this.context, "Saved", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_EndTimeFragment_to_SettingsFragment)
        }
    }

    override fun onResume() {
        super.onResume()

        this.updateUI()
    }

    private fun updateUI() {
        this.context?.let {
            val endTime = SettingsManager.getSetting(it, SettingsManager.END_TIME, 22)
            binding.timePicker.hour = endTime
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}