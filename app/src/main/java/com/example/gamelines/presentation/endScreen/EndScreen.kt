package com.example.gamelines.presentation.endScreen

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamelines.databinding.EndScreenFragmentBinding

class EndScreen : Fragment() {
	private lateinit var binding: EndScreenFragmentBinding
	private lateinit var viewModel: EndScreenViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = EndScreenFragmentBinding.inflate(layoutInflater)
		viewModel = ViewModelProvider(this).get(EndScreenViewModel::class.java)
		viewModel.initFragment(this)
		binding.buttonBack.setOnClickListener { viewModel.onButtonBackClick() }
		return binding.root
	}

	companion object {
		fun newInstance() = EndScreen()
	}

}