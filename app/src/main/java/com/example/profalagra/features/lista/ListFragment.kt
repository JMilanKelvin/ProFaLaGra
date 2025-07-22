package com.example.profalagra.features.lista

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.profalagra.R
import com.example.profalagra.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayEmptyMessage()
    }

    private fun displayEmptyMessage(){
        binding.infoTitle = getString(R.string.recent_graphics)
        binding.infoBody = getString(R.string.empty_list_message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}