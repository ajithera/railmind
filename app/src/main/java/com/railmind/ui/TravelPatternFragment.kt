package com.railmind.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.railmind.R
import com.railmind.viewmodel.TravelPatternViewModel

class TravelPatternFragment : Fragment() {

    private lateinit var viewModel: TravelPatternViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_travel_pattern, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(TravelPatternViewModel::class.java)

        // Initialize UI components and set up listeners here
        // For example, set up day selection and time range pickers
    }
}