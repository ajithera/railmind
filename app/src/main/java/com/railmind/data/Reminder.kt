package com.railmind.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val travelDate: Date,
    val reminderDate: Date,
    val travelPattern: String,
    val notificationTime: String = "7:15 AM",
    val isEnabled: Boolean = true,
    val departureDay: String,
    val returnDay: String
) : Parcelable