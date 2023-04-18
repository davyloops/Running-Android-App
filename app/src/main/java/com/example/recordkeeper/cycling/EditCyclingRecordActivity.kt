package com.example.recordkeeper.cycling

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import com.example.recordkeeper.databinding.ActivityEditCyclingRecordBinding
import com.example.recordkeeper.databinding.ActivityEditRunningRecordBinding

class EditCyclingRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditCyclingRecordBinding
    private val cyclingPreferences: SharedPreferences by lazy {
        getSharedPreferences("cycling", Context.MODE_PRIVATE)
    }
    private val cyclingCategory: String? by lazy { intent.getStringExtra("Category") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCyclingRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()
        displayRecord(cyclingCategory)
    }

    private fun setupUi() {
        title = "$cyclingCategory Record"

        binding.buttonSave.setOnClickListener {
            saveRecord(cyclingCategory)
            finish()
        }

        binding.buttonDelete.setOnClickListener {
            clearRecord()
            finish()
        }
    }

    private fun clearRecord() {
        cyclingPreferences.edit {
            remove("$cyclingCategory record")
            remove("$cyclingCategory date")
        }
    }

    private fun displayRecord(cyclingCategory: String?) {
        binding.editTextRecord.setText(cyclingPreferences.getString("$cyclingCategory record", null))
        binding.editTextDate.setText(cyclingPreferences.getString("$cyclingCategory date", null))
    }

    private fun saveRecord(cyclingCategory: String?) {
        val record = binding.editTextRecord.text.toString()
        val date = binding.editTextDate.text.toString()

        val cyclingPreferences = getSharedPreferences("cycling", Context.MODE_PRIVATE)

        cyclingPreferences.edit {
            putString("$cyclingCategory record", record)
            putString("$cyclingCategory date", date)
        }
    }
}