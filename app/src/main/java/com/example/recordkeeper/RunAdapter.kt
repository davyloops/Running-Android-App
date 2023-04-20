package com.example.recordkeeper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RunAdapter(private val runs: Array<Run>): RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunAdapter.RunViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_run, parent, false)
        return RunViewHolder(view)
    }

    override fun onBindViewHolder(holder: RunAdapter.RunViewHolder, position: Int) {
        holder.bind(runs[position])
    }

    override fun getItemCount(): Int {
        return runs.size
    }

    inner class RunViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val dateTextView = itemView.findViewById<TextView>(R.id.text_view_run_item_date)
        val distanceTextView = itemView.findViewById<TextView>(R.id.text_view_run_item_distance)
        val timeTextView = itemView.findViewById<TextView>(R.id.text_view_run_item_time)
        val averageSpeedTextView = itemView.findViewById<TextView>(R.id.text_view_run_item_average_speed)

        fun bind(run: Run) {
            dateTextView.text = run.date
            distanceTextView.text = run.distance
            timeTextView.text = run.time.toString()
            averageSpeedTextView.text = run.averageSpeed.toString()
        }
    }

    fun bind(run: Run) {

    }
}