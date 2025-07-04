package com.railmind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.railmind.data.Reminder
import com.railmind.data.ReminderDao
import kotlinx.coroutines.launch
import java.util.*

class TravelPatternViewModel(
    private val reminderDao: ReminderDao
) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> = _navigateBack

    fun createTravelPattern(departureDay: String, returnDay: String, monthsRange: Int) {
        viewModelScope.launch {
            try {
                val calendar = Calendar.getInstance()
                val endDate = Calendar.getInstance().apply {
                    add(Calendar.MONTH, monthsRange)
                }

                while (calendar.before(endDate)) {
                    // Create departure reminder
                    if (isDayMatch(calendar, departureDay)) {
                        createReminder(calendar.time, "Departure")
                    }

                    // Create return reminder
                    if (isDayMatch(calendar, returnDay)) {
                        createReminder(calendar.time, "Return")
                    }

                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }

                _navigateBack.value = true
            } catch (e: Exception) {
                _error.value = "Failed to create travel pattern: ${e.localizedMessage}"
            }
        }
    }

    private fun isDayMatch(calendar: Calendar, dayName: String): Boolean {
        val dayOfWeek = when (dayName) {
            "Monday" -> Calendar.MONDAY
            "Tuesday" -> Calendar.TUESDAY
            "Wednesday" -> Calendar.WEDNESDAY
            "Thursday" -> Calendar.THURSDAY
            "Friday" -> Calendar.FRIDAY
            "Saturday" -> Calendar.SATURDAY
            "Sunday" -> Calendar.SUNDAY
            else -> return false
        }
        return calendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek
    }

    private suspend fun createReminder(date: Date, type: String) {
        val reminder = Reminder(
            id = 0,
            travelDate = date,
            travelPattern = "$type Journey",
            isEnabled = true,
            createdAt = Date()
        )
        reminderDao.insertReminder(reminder)
    }

    fun doneNavigating() {
        _navigateBack.value = false
    }
}