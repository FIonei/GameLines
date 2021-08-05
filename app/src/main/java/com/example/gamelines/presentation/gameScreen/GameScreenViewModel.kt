package com.example.gamelines.presentation.gameScreen

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.gamelines.R

class GameScreenViewModel : ViewModel() {
	private lateinit var fragment: GameScreen
	private lateinit var navController: NavController

	fun initFragment(gameScreen: GameScreen) {
		fragment = gameScreen
		navController = NavController(fragment.requireContext())
	}

	fun onButtonNextClick() {
		navController.navigate(R.id.action_gameScreen_to_endScreen)
	}

	fun onButtonBackClick() {
		navController.popBackStack()
	}
}