package com.railmind.workers

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit
import java.util.Calendar

object ReminderWorkerManager {
    private const val REMINDER_WORK_NAME = "reminder_work"

    fun scheduleRemindersCheck(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
            1, TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            REMINDER_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            repeatingRequest
        )
    }

    fun cancelRemindersCheck(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(REMINDER_WORK_NAME)
    }

    fun scheduleReminderWork(context: Context, hour: Int, minute: Int) {
        // Cancel any existing work
        WorkManager.getInstance(context).cancelUniqueWork(REMINDER_WORK_NAME)

        // Calculate initial delay
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        if (currentHour > hour || (currentHour == hour && currentMinute >= minute)) {
            // If the target time has passed today, schedule for tomorrow
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        val now = Calendar.getInstance()
        val initialDelay = calendar.timeInMillis - now.timeInMillis

        // Create work request
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val reminderWorkRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
            24, TimeUnit.HOURS,
            initialDelay, TimeUnit.MILLISECONDS
        )
            .setConstraints(constraints)
            .build()

        // Enqueue unique work
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                REMINDER_WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                reminderWorkRequest
            )
    }

    fun cancelReminderWork(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(REMINDER_WORK_NAME)
    }
}