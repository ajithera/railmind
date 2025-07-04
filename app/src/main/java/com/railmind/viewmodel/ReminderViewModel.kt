package com.railmind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.railmind.data.Reminder
import com.railmind.data.ReminderDao

class ReminderViewModel(private val reminderDao: ReminderDao) : ViewModel() {

    private val _reminders = MutableLiveData<List<Reminder>>()
    val reminders: LiveData<List<Reminder>> get() = _reminders

    init {
        loadReminders()
    }

    private fun loadReminders() {
        // Load reminders from the database
        _reminders.value = reminderDao.getAllReminders()
    }

    fun addReminder(reminder: Reminder) {
        reminderDao.insert(reminder)
        loadReminders()
    }

    fun deleteReminder(reminder: Reminder) {
        reminderDao.delete(reminder)
        loadReminders()
    }

    fun updateReminder(reminder: Reminder) {
        reminderDao.update(reminder)
        loadReminders()
    }
}