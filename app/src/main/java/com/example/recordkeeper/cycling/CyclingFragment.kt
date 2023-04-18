package com.example.recordkeeper.cycling

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recordkeeper.databinding.FragmentCyclingBinding
import com.example.recordkeeper.editrecord.EditRecordActivity

class CyclingFragment : Fragment() {

    private lateinit var binding: FragmentCyclingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCyclingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        displayRecords()
    }

    private fun displayRecords() {
        val recordPreferences = requireContext().getSharedPreferences("records", Context.MODE_PRIVATE)

        binding.textViewLongestRideValue.text = recordPreferences.getString("Longest Ride record", null)
        binding.textViewLongestRideDate.text = recordPreferences.getString("Longest Ride date", null)
        binding.textViewBiggestClimbValue.text = recordPreferences.getString("Biggest Climb record", null)
        binding.textViewBiggestClimbDate.text = recordPreferences.getString("Biggest Climb date", null)
        binding.textViewBestAverageSpeedValue.text = recordPreferences.getString("Best Average Speed record", null)
        binding.textViewBestAverageSpeedDate.text = recordPreferences.getString("Best Average Speed date", null)
    }

    private fun setupClickListeners() {
        binding.containerLongestRide.setOnClickListener { launchCyclingRecordsScreen("Longest Ride") }
        binding.containerBiggestClimb.setOnClickListener { launchCyclingRecordsScreen("Biggest Climb") }
        binding.containerBestAverageSpeed.setOnClickListener { launchCyclingRecordsScreen("Best Average Speed") }
    }

    private fun launchCyclingRecordsScreen(category: String) {
        val intent = Intent(context, EditRecordActivity::class.java)
        intent.putExtra("Category", category)
        startActivity(intent)
    }
}