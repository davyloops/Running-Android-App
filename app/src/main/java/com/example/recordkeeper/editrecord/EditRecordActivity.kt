package com.example.recordkeeper.editrecord

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private val ID: String? by lazy { intent.getStringExtra("Id") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()
    }

    private fun setupUi() {
        title = "$date Run"

        binding.buttonSave.setOnClickListener {
            saveRecord()
            finish()
        }

        binding.buttonDelete.setOnClickListener {
            clearRecord()
            finish()
        }

        if (ID != null) displayRecord()
    }

    private fun clearRecord() {
        val runs = getRuns()
        runs?.remove(getRun()!!)

        if (ID != null) ModelPreferencesManager.remove("$date")
        ModelPreferencesManager.put(runs, "$date")
    }

    private fun displayRecord() {
        val run = getRun()

        binding.editTextDistance.setText(run?.distance.toString(), null)
        binding.editTextTime.setText(run?.time.toString(), null)

    }

    private fun saveRecord() {
        createRuns()

        val runs = getRuns()

        val runId = if (ID != null) ID else createRunId()
        val distance = binding.editTextDistance.text.toString().toFloat()
        val time = binding.editTextTime.text.toString().toInt()

        val run = Run(runId!!, "$date", distance, time)
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

    private fun getRun(): Run? {
        val runs = getRuns() ?: return null

        for (run in runs.runs) {
            if (run.id == ID) {
                return run
            }
        }

        return null
    }

    private fun createRunId(): String {
        val runs = getRuns()
        return "$date-${runs?.size}"
    }
}