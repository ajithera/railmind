package com.railmind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.railmind.data.Reminder
import com.railmind.data.ReminderDao
import kotlinx.coroutines.launch
import java.util.*

class ReminderViewModel(
    private val reminderDao: ReminderDao
) : ViewModel() {

    private val _reminders = MutableLiveData<List<Reminder>>()
    val reminders: LiveData<List<Reminder>> = _reminders

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadReminders()
    }

    fun loadReminders() {
        viewModelScope.launch {
            try {
                _reminders.value = reminderDao.getAllReminders()
            } catch (e: Exception) {
                _error.value = "Failed to load reminders: ${e.localizedMessage}"
            }
        }
    }

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            try {
                reminderDao.deleteReminder(reminder)
                loadReminders()
            } catch (e: Exception) {
                _error.value = "Failed to delete reminder: ${e.localizedMessage}"
            }
        }
    }

    fun updateReminder(reminder: Reminder) {
        viewModelScope.launch {
            try {
                reminderDao.updateReminder(reminder)
                loadReminders()
            } catch (e: Exception) {
                _error.value = "Failed to update reminder: ${e.localizedMessage}"
            }
        }
    }

    fun getUpcomingReminders(): List<Reminder> {
        return reminders.value?.filter { reminder ->
            reminder.travelDate.after(Date())
        } ?: emptyList()
    }

    fun clearError() {
        _error.value = null
    }
}