package com.example.profalagra.features.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.profalagra.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val PREFS_NAME = "app_preferences"
    private val THEME_KEY = "theme_mode"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedMode = prefs.getInt(THEME_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        val selectedIndex = when (savedMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> 0
            AppCompatDelegate.MODE_NIGHT_YES -> 1
            else -> 2
        }
        binding.spinnerTheme.setSelection(selectedIndex)

        binding.spinnerTheme.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val mode = when (position) {
                    0 -> AppCompatDelegate.MODE_NIGHT_NO       // Claro
                    1 -> AppCompatDelegate.MODE_NIGHT_YES      // Oscuro
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
                if (AppCompatDelegate.getDefaultNightMode() != mode) {
                    AppCompatDelegate.setDefaultNightMode(mode)

                    prefs.edit()
                        .putInt(THEME_KEY, mode)
                        .putInt("last_selected_index", 2)
                        .apply()

//                    AppCompatDelegate.setDefaultNightMode(mode)
                    activity?.recreate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}