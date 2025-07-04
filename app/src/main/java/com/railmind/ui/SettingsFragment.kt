package com.railmind.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.railmind.databinding.FragmentSettingsBinding
import com.railmind.workers.ReminderWorkerManager

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        binding.notificationSwitch.isChecked = isGranted
        if (isGranted) {
            ReminderWorkerManager.scheduleReminders(requireContext())
        } else {
            ReminderWorkerManager.cancelReminders(requireContext())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadSettings()
        setupNotificationSwitch()
        setupSaveButton()
    }

    private fun loadSettings() {
        val sharedPrefs = requireContext().getSharedPreferences("railmind_settings", Context.MODE_PRIVATE)

        // Load notification preference
        binding.notificationSwitch.isChecked = sharedPrefs.getBoolean("notifications_enabled", true)

        // Load reminder time
        val hour = sharedPrefs.getInt("reminder_hour", 7)
        val minute = sharedPrefs.getInt("reminder_minute", 15)
        binding.reminderTimePicker.apply {
            setIs24HourView(false)
            setHour(hour)
            setMinute(minute)
        }
    }

    private fun setupNotificationSwitch() {
        binding.notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun setupSaveButton() {
        binding.saveSettingsButton.setOnClickListener {
            saveSettings()
            findNavController().navigateUp()
        }
    }

    private fun saveSettings() {
        val sharedPrefs = requireContext().getSharedPreferences("railmind_settings", Context.MODE_PRIVATE)
        sharedPrefs.edit().apply {
            putBoolean("notifications_enabled", binding.notificationSwitch.isChecked)
            putInt("reminder_hour", binding.reminderTimePicker.hour)
            putInt("reminder_minute", binding.reminderTimePicker.minute)
        }.apply()

        if (binding.notificationSwitch.isChecked) {
            ReminderWorkerManager.scheduleReminders(requireContext())
        } else {
            ReminderWorkerManager.cancelReminders(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}