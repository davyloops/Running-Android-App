package com.example.recordkeeper.editrecord

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import com.example.recordkeeper.databinding.ActivityEditRecordBinding

class EditRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditRecordBinding
    private val runningPreferences: SharedPreferences by lazy {
        getSharedPreferences("records", Context.MODE_PRIVATE)
    }
    private val category: String? by lazy { intent.getStringExtra("Category") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()
    }

    private fun setupUi() {
        title = "$category Record"

        displayRecord(category)
        binding.buttonSave.setOnClickListener {
            saveRecord(category)
            finish()
        }

        binding.buttonDelete.setOnClickListener {
            clearRecord()
            finish()
        }
    }

    private fun clearRecord() {
        runningPreferences.edit {
            remove("$category record")
            remove("$category date")
        }
    }

    private fun displayRecord(category: String?) {
        binding.editTextRecord.setText(runningPreferences.getString("$category record", null))
        binding.editTextDate.setText(runningPreferences.getString("$category date", null))
    }

    private fun saveRecord(category: String?) {
        val record = binding.editTextRecord.text.toString()
        val date = binding.editTextDate.text.toString()

        val runningPreferences = getSharedPreferences("records", Context.MODE_PRIVATE)

        runningPreferences.edit {
            putString("$category record", record)
            putString("$category date", date)
        }
    }
}