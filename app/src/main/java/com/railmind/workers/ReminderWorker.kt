package com.railmind.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.railmind.RailMindApplication
import com.railmind.utils.DateUtils
import com.railmind.utils.NotificationUtils
import java.util.*

class ReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val reminderDao = (context.applicationContext as RailMindApplication)
        .database.reminderDao()

    override suspend fun doWork(): Result {
        try {
            val today = Calendar.getInstance().time
            val reminders = reminderDao.getAllReminders()
            
            reminders.filter { reminder ->
                reminder.isEnabled &&
                DateUtils.calculateReminderDate(Calendar.getInstance().apply {
                    time = reminder.travelDate
                }).time.time <= today.time
            }.forEach { reminder ->
                NotificationUtils.showReminderNotification(
                    applicationContext,
                    reminder
                )
            }
            
            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }
    }
}