package com.example.recordkeeper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recordkeeper.databinding.FragmentMapsBinding
import com.example.recordkeeper.databinding.FragmentRunningLogBinding


class RunningLogFragment : Fragment() {

    private lateinit var binding: FragmentRunningLogBinding

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
        val list: RecyclerView = binding.recyclerView

        val runs: Array<Run> = arrayOf(
            Run("4-20-2023", "100", 20, 20),
            Run("4-20-2023", "100", 20, 20),
            Run("4-20-2023", "100", 20, 20),
            Run("4-20-2023", "100", 20, 20),
            Run("4-20-2023", "100", 20, 20),
            Run("4-20-2023", "100", 20, 20),
            Run("4-20-2023", "50", 10, 25)
        )

        val adapter: RunAdapter = RunAdapter(runs)
        list.adapter = adapter
    }
}