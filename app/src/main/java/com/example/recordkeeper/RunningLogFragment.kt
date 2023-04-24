package com.example.recordkeeper

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.Display.Mode
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.recordkeeper.databinding.FragmentRunningLogBinding
import com.example.recordkeeper.editrecord.EditRecordActivity
import java.text.SimpleDateFormat
import java.util.*


class RunningLogFragment : Fragment(), RecyclerViewInterface {

    private lateinit var binding: FragmentRunningLogBinding
    private var selectedDate: String = getCurrentDate()
    private var runs: Runs? = getRuns()
    private lateinit var list: RecyclerView
    private lateinit var adapter: RunAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunningLogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onStart() {
        super.onStart()

        binding.calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            selectedDate = "$year-${month + 1}-$dayOfMonth"
            refreshRecyclerView()
        }

        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-M-d")
        selectedDate = formatter.format(time)

        list = binding.recyclerView

        adapter = RunAdapter(runs!!, this)
        list.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        refreshRecyclerView()
    }

    private fun refreshRecyclerView() {
        val newRuns = getRuns()
        setRuns(newRuns)
        displayRuns()
    }

    fun getSelectedDate(): String {
        return selectedDate
    }

    private fun getCurrentDate(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-M-d")
        return formatter.format(time)
    }

    private fun getRuns(): Runs? {
        if (ModelPreferencesManager.contains(selectedDate)) {
            return ModelPreferencesManager.get<Runs>(selectedDate)
        }
        return Runs(mutableListOf())
    }

    private fun setRuns(newRuns: Runs?) {
        runs?.clear()

        if (newRuns != null) {
            for (run in newRuns.runs) {
                runs?.add(run)
            }
        }
    }

    private fun displayRuns() {
        if (runs == null) {
            list.isVisible = false
            adapter.notifyDataSetChanged()
        }
        else {
            list.isVisible = true
            adapter.notifyDataSetChanged()
        }
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(activity, EditRecordActivity::class.java)
        intent.putExtra("Date", selectedDate)
        intent.putExtra("Id", runs?.get(position)?.id)
        intent.putExtra("Is Editing Run", true)
        startActivity(intent)
    }
}