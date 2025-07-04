package com.railmind.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val travelDate: Date,
    val reminderDate: Date,
    val notificationTime: String = "07:15 AM",
    val travelPattern: String
)