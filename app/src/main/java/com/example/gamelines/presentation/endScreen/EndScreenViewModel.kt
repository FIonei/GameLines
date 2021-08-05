package com.example.gamelines.presentation.endScreen

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

class EndScreenViewModel : ViewModel() {
	private lateinit var fragment: EndScreen
	private lateinit var navController: NavController

	fun initFragment(endScreen: EndScreen) {
		fragment = endScreen
		navController = NavController(fragment.requireContext())
	}

	fun onButtonBackClick() {
		navController.popBackStack()
	}
}