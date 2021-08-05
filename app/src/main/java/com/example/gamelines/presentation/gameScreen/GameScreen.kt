package com.example.gamelines.presentation.gameScreen

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamelines.databinding.GameScreenFragmentBinding

class GameScreen : Fragment() {
	private lateinit var binding: GameScreenFragmentBinding
	private lateinit var viewModel: GameScreenViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = GameScreenFragmentBinding.inflate(layoutInflater)
		viewModel = ViewModelProvider(this).get(GameScreenViewModel::class.java)
		viewModel.initFragment(this)
		binding.buttonNext.setOnClickListener { viewModel.onButtonNextClick() }
		binding.buttonBack.setOnClickListener { viewModel.onButtonBackClick() }
		return binding.root
	}

	companion object {
		fun newInstance() = GameScreen()
	}
}