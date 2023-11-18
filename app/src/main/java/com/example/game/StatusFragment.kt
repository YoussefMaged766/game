package com.example.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.game.databinding.FragmentStatusBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatusFragment : Fragment() {

    private lateinit var binding: FragmentStatusBinding
    private val viewModel: StatusViewModel by viewModels()
    private val adapter: GameAdapter by lazy { GameAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            initRV()
        }
    }

    private suspend fun initRV() {
        viewModel.stateProduct.collect {
            if (it.isEmpty()) {
                binding.noStatsTextView.visibility = View.VISIBLE
            } else {
                binding.noStatsTextView.visibility = View.GONE
            }
            adapter.submitList(it)
            binding.statusRecyclerView.adapter = adapter
        }

    }


}