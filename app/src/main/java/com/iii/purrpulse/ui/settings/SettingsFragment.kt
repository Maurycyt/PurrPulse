package com.iii.purrpulse.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.iii.purrpulse.R
import com.iii.purrpulse.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var highQualityButton: ToggleButton
    private val highQualityPreferenceID: String = "isHighQualityOn"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Get button
        highQualityButton = root.findViewById(R.id.quality_toggle_button)

        // Set button state from persistent storage.
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val isHighQualityOn = sharedPreferences.getBoolean(highQualityPreferenceID, false)
        highQualityButton.isChecked = isHighQualityOn
        settingsViewModel.setHighQuality(isHighQualityOn)

        // Set persistent storage on button update
        highQualityButton.setOnClickListener {
            settingsViewModel.setHighQuality(highQualityButton.isChecked)

            val editor = sharedPreferences.edit()
            editor.putBoolean(highQualityPreferenceID, settingsViewModel.isHighQualityOn.value ?: false)
            editor.apply()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}