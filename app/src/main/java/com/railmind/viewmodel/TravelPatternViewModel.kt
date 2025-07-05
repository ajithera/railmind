package com.railmind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.railmind.data.Reminder
import com.railmind.data.ReminderDao
import com.railmind.utils.DateUtils
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
                        createReminder(calendar.time, "Departure", departureDay, returnDay)
                    }

                    // Create return reminder
                    if (isDayMatch(calendar, returnDay)) {
                        createReminder(calendar.time, "Return", departureDay, returnDay)
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

    private suspend fun createReminder(travelDate: Date, type: String, departureDay: String, returnDay: String) {
        // 1. Convert the travel Date to a Calendar
        val travelCalendar = Calendar.getInstance().apply { time = travelDate }

        // 2. Call your utility function with the Calendar
        val reminderDateFromUtils = DateUtils.calculateReminderDate(travelCalendar)

        val reminder = Reminder(
            id = 0, // ID is auto-generated
            travelDate = travelDate,
            // 3. Convert the result back to a Date for the database entity
            reminderDate = reminderDateFromUtils.time,
            travelPattern = "$type Journey",
            isEnabled = true,
            departureDay = departureDay,
            returnDay = returnDay
        )
        reminderDao.insertReminder(reminder)
    }

    fun doneNavigating() {
        _navigateBack.value = false
    }
}
