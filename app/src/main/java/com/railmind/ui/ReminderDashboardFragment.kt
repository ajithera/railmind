package com.railmind.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.railmind.R
import com.railmind.data.Reminder
import com.railmind.viewmodel.ReminderViewModel

class ReminderDashboardFragment : Fragment() {

    private lateinit var reminderViewModel: ReminderViewModel
    private lateinit var reminderAdapter: ReminderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reminder_dashboard, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_reminders)
        reminderAdapter = ReminderAdapter { reminder -> onReminderClicked(reminder) }
        recyclerView.adapter = reminderAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        reminderViewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)
        reminderViewModel.allReminders.observe(viewLifecycleOwner, Observer { reminders ->
            reminders?.let { reminderAdapter.submitList(it) }
        })

        return view
    }

    private fun onReminderClicked(reminder: Reminder) {
        // Handle reminder click (edit or delete)
    }
}