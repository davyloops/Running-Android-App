package com.example.recordkeeper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
}