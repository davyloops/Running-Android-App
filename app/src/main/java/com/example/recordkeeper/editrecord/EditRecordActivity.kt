package com.example.recordkeeper.editrecord

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import com.example.recordkeeper.MainActivity
import com.example.recordkeeper.ModelPreferencesManager
import com.example.recordkeeper.Run
import com.example.recordkeeper.Runs
import com.example.recordkeeper.databinding.ActivityEditRecordBinding

class EditRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditRecordBinding
    private val runningPreferences: SharedPreferences by lazy {
        getSharedPreferences("log", Context.MODE_PRIVATE)
    }
    private val date: String? by lazy { intent.getStringExtra("Date") }
    private val isEditingRun: Boolean? by lazy { intent.getBooleanExtra("Is Editing Run", false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()
    }

    private fun setupUi() {
        title = "$date Run"

        displayRecord(date)
        binding.buttonSave.setOnClickListener {
            saveRecord()
            finish()
        }

        binding.buttonDelete.setOnClickListener {
            clearRecord()
            finish()
        }
    }

    private fun clearRecord() {
        runningPreferences.edit {
            remove("$date record")
            remove("$date date")
        }
    }

    private fun displayRecord(category: String?) {
        binding.editTextDistance.setText(runningPreferences.getString("$date record", null))
        binding.editTextTime.setText(runningPreferences.getString("$date date", null))
    }

    private fun saveRecord() {

        createRuns()
        val recordName = nameRecord()
        val runs = getRuns()


        val distance = binding.editTextDistance.text.toString().toFloat()
        val time = binding.editTextTime.text.toString().toInt()

        val run = Run(recordName, "$date", distance, time)
        runs?.add(run)

        ModelPreferencesManager.put(runs, "$date")
    }

    private fun createRuns() {
        if (!ModelPreferencesManager.contains("$date")) {
            ModelPreferencesManager.put(Runs(mutableListOf()), "$date")
        }
    }

    private fun getRuns(): Runs? {
        if (ModelPreferencesManager.contains("$date")) {
            return ModelPreferencesManager.get<Runs>("$date")
        }
        return null
    }

    private fun nameRecord(): String {
        val runs = getRuns()
        return "$date-${runs?.size}"
    }
}