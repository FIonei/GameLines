package com.example.gamelines.presentation.optionsScreen

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamelines.databinding.OptionsScreenFragmentBinding

class OptionsScreen : Fragment() {
	private lateinit var binding: OptionsScreenFragmentBinding
	private lateinit var viewModel: OptionsScreenViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = OptionsScreenFragmentBinding.inflate(layoutInflater)
		viewModel = ViewModelProvider(this).get(OptionsScreenViewModel::class.java)
		viewModel.initFragment(this)
		binding.buttonNext.setOnClickListener { viewModel.onButtonNextClick() }
		binding.buttonNext
		return binding.root
	}

	companion object {
		fun newInstance() = OptionsScreen()
	}

}