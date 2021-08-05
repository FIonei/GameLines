package com.example.gamelines.presentation.optionsScreen

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.gamelines.R

class OptionsScreenViewModel : ViewModel() {
	private lateinit var fragment: OptionsScreen
	private lateinit var navController: NavController

	fun initFragment(optionsScreen: OptionsScreen) {
		fragment = optionsScreen
		navController = NavController(fragment.requireContext())
	}

	fun onButtonNextClick() {
		navController.navigate(R.id.action_optionsScreen_to_gameScreen)
	}
}