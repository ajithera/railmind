package com.railmind.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.railmind.data.AppDatabase
import com.railmind.databinding.FragmentTravelPatternBinding
import com.railmind.viewmodel.TravelPatternViewModel
import com.railmind.viewmodel.TravelPatternViewModelFactory

class TravelPatternFragment : Fragment() {

    private var _binding: FragmentTravelPatternBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TravelPatternViewModel

    private val weekDays = listOf(
        "Monday", "Tuesday", "Wednesday", "Thursday",
        "Friday", "Saturday", "Sunday"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelPatternBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupSpinners()
        setupSeekBar()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupViewModel() {
        val database = AppDatabase.getDatabase(requireContext())
        val factory = TravelPatternViewModelFactory(database.reminderDao())
        viewModel = ViewModelProvider(this, factory)[TravelPatternViewModel::class.java]
    }

    private fun setupSpinners() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            weekDays
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.departureDaySpinner.adapter = adapter
        binding.returnDaySpinner.adapter = adapter
    }

    private fun setupSeekBar() {
        binding.timeRangeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val months = progress + 1
                binding.timeRangeText.text = "$months months"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun setupClickListeners() {
        binding.setReminderButton.setOnClickListener {
            val departureDay = binding.departureDaySpinner.selectedItem as String
            val returnDay = binding.returnDaySpinner.selectedItem as String
            val monthsRange = binding.timeRangeSeekBar.progress + 1

            viewModel.createTravelPattern(departureDay, returnDay, monthsRange)
        }
    }

    private fun observeViewModel() {
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.navigateBack.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                findNavController().navigateUp()
                viewModel.doneNavigating()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}