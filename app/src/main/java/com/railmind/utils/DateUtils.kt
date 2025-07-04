package com.railmind.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {

    fun calculateReminderDate(travelDate: Calendar): Calendar {
        val reminderDate = Calendar.getInstance()
        reminderDate.timeInMillis = travelDate.timeInMillis
        reminderDate.add(Calendar.DAY_OF_MONTH, -60)
        return reminderDate
    }

    fun formatDate(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun isDateInRange(startDate: Calendar, endDate: Calendar, targetDate: Calendar): Boolean {
        return targetDate.after(startDate) && targetDate.before(endDate)
    }
}