package com.railmind.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    private const val REMINDER_DAYS_BEFORE = 2
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun calculateReminderDate(travelDate: Calendar): Calendar {
        val reminderDate = travelDate.clone() as Calendar
        reminderDate.add(Calendar.DAY_OF_YEAR, -1) // Notify one day before
        reminderDate.set(Calendar.HOUR_OF_DAY, 20) // Set notification time to 8 PM
        reminderDate.set(Calendar.MINUTE, 0)
        reminderDate.set(Calendar.SECOND, 0)
        return reminderDate
    }

    fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }

    fun formatTime(calendar: Calendar): String {
        return timeFormat.format(calendar.time)
    }

    fun parseTime(timeString: String): Calendar {
        val calendar = Calendar.getInstance()
        timeFormat.parse(timeString)?.let { time ->
            calendar.time = time
        }
        return calendar
    }

    fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
}