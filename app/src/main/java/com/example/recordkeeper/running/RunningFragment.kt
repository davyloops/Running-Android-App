package com.example.recordkeeper.running

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recordkeeper.databinding.FragmentRunningBinding
import com.example.recordkeeper.editrecord.EditRecordActivity

class RunningFragment : Fragment() {

    private lateinit var binding: FragmentRunningBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunningBinding.inflate(layoutInflater, container, false)
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

    private fun setupClickListeners() {
        binding.container5km.setOnClickListener { launchRunningRecordsScreen("5km") }
        binding.container10km.setOnClickListener { launchRunningRecordsScreen("10km") }
        binding.containerHalfMarathon.setOnClickListener { launchRunningRecordsScreen("Half Marathon") }
        binding.containerMarathon.setOnClickListener { launchRunningRecordsScreen("Marathon") }
    }

    private fun displayRecords() {
        val recordPreferences = requireContext().getSharedPreferences("records", Context.MODE_PRIVATE)

        binding.textView5kmValue.text = recordPreferences.getString("5km record", null)
        binding.textView5kmDate.text = recordPreferences.getString("5km date", null)
        binding.textView10kmValue.text = recordPreferences.getString("10km record", null)
        binding.textView10kmDate.text = recordPreferences.getString("10km date", null)
        binding.textViewHalfMarathonValue.text = recordPreferences.getString("Half Marathon record", null)
        binding.textViewHalfMarathonDate.text = recordPreferences.getString("Half Marathon date", null)
        binding.textViewMarathonValue.text = recordPreferences.getString("Marathon record", null)
        binding.textViewMarathonDate.text = recordPreferences.getString("Marathon date", null)
    }

    private fun launchRunningRecordsScreen(category: String) {
        val intent = Intent(context, EditRecordActivity::class.java)
        intent.putExtra("Category", category)
        startActivity(intent)
    }
}