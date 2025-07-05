package com.railmind.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.railmind.data.AppDatabase
import com.railmind.data.Reminder
import com.railmind.databinding.FragmentReminderDashboardBinding
import com.railmind.viewmodel.ReminderViewModel
import com.railmind.viewmodel.ReminderViewModelFactory

class ReminderDashboardFragment : Fragment() {

    private var _binding: FragmentReminderDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var reminderViewModel: ReminderViewModel
    private lateinit var reminderAdapter: ReminderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReminderDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupViewModel()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        reminderAdapter = ReminderAdapter { reminder -> onReminderClicked(reminder) }
        binding.reminderRecyclerView.apply {
            adapter = reminderAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupViewModel() {
        val database = AppDatabase.getDatabase(requireContext())
        val factory = ReminderViewModelFactory(database.reminderDao())
        reminderViewModel = ViewModelProvider(this, factory)[ReminderViewModel::class.java]
    }

    private fun observeViewModel() {
        reminderViewModel.reminders.observe(viewLifecycleOwner) { reminders ->
            reminderAdapter.submitList(reminders)
            binding.emptyStateText.visibility = if (reminders.isEmpty()) View.VISIBLE else View.GONE
        }

        reminderViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.addReminderButton.setOnClickListener {
            findNavController().navigate(
                ReminderDashboardFragmentDirections.actionDashboardToPattern()
            )
        }

        binding.settingsButton.setOnClickListener {
            findNavController().navigate(
                ReminderDashboardFragmentDirections.actionDashboardToSettings()
            )
        }
    }

    private fun onReminderClicked(reminder: Reminder) {
        // 1. Create an instance of the bottom sheet, passing the whole Reminder object
        val bottomSheet = ReminderOptionsBottomSheet.newInstance(reminder)

        // 2. Set the listener for the delete button.
        // This code block will run when the delete button inside the sheet is clicked.
        bottomSheet.setOnDeleteClickListener { reminderToDelete ->
            reminderViewModel.deleteReminder(reminderToDelete)
        }

        // 3. Set the listener for the edit button.
        bottomSheet.setOnEditClickListener { _ -> // Renamed 'reminderToEdit' to '_'
            // TODO: Handle the edit logic, for example:
            // val action = ReminderDashboardFragmentDirections.actionDashboardToEditFragment(reminderToEdit)
            // findNavController().navigate(action)
        }

        // 4. Show the configured bottom sheet
        bottomSheet.show(childFragmentManager, "reminder_options")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}