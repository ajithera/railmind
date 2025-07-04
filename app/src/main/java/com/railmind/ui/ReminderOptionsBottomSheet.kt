package com.railmind.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.railmind.data.Reminder
import com.railmind.databinding.BottomSheetReminderOptionsBinding

class ReminderOptionsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetReminderOptionsBinding? = null
    private val binding get() = _binding!!

    private var onDeleteClickListener: ((Reminder) -> Unit)? = null
    private var onEditClickListener: ((Reminder) -> Unit)? = null
    private lateinit var reminder: Reminder

    companion object {
        fun newInstance(reminder: Reminder) = ReminderOptionsBottomSheet().apply {
            this.reminder = reminder
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetReminderOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.editButton.setOnClickListener {
            onEditClickListener?.invoke(reminder)
            dismiss()
        }

        binding.deleteButton.setOnClickListener {
            onDeleteClickListener?.invoke(reminder)
            dismiss()
        }
    }

    fun setOnDeleteClickListener(listener: (Reminder) -> Unit) {
        onDeleteClickListener = listener
    }

    fun setOnEditClickListener(listener: (Reminder) -> Unit) {
        onEditClickListener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}