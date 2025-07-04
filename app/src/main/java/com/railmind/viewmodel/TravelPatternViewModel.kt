package com.railmind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class TravelPatternViewModel : ViewModel() {

    private val _selectedTravelDays = MutableLiveData<List<DayOfWeek>>()
    val selectedTravelDays: LiveData<List<DayOfWeek>> get() = _selectedTravelDays

    private val _travelStartDate = MutableLiveData<Date>()
    val travelStartDate: LiveData<Date> get() = _travelStartDate

    private val _travelEndDate = MutableLiveData<Date>()
    val travelEndDate: LiveData<Date> get() = _travelEndDate

    private val _reminderTime = MutableLiveData<Calendar>()
    val reminderTime: LiveData<Calendar> get() = _reminderTime

    fun setTravelDays(days: List<DayOfWeek>) {
        _selectedTravelDays.value = days
    }

    fun setTravelStartDate(date: Date) {
        _travelStartDate.value = date
    }

    fun setTravelEndDate(date: Date) {
        _travelEndDate.value = date
    }

    fun setReminderTime(calendar: Calendar) {
        _reminderTime.value = calendar
    }
}