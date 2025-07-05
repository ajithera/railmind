package com.railmind.workers

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object ReminderWorkerManager {

    private const val REMINDER_WORK_TAG = "reminder_work"

    fun scheduleReminders(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            REMINDER_WORK_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }

    fun cancelReminders(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(REMINDER_WORK_TAG)
    }
}
