package com.romkapo.todoapp.utils.navigation

import AuthScreen
import android.app.Application
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.romkapo.todoapp.appComponent
import com.romkapo.todoapp.di.components.common.Inject
import com.romkapo.todoapp.presentation.screen.addedititem.AddEditScreen
import com.romkapo.todoapp.presentation.screen.settings.SettingsScreen
import com.romkapo.todoapp.presentation.screen.todolistitems.TodoListScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = "auth") {
        composable("todo_list",
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(2000))
            },
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(2000))
            }) {
            val todoFragmentComponent = (LocalContext.current.applicationContext as Application)
                .appComponent.todoItemListFragmentComponentFactory().create()
            Inject(todoFragmentComponent.getViewModelFactory()) {
                TodoListScreen(
                    navController
                )
            }
        }
        composable(
            "add_edit?id={id}",
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(2000))
            },
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(2000))
            },
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                nullable = true
                defaultValue = null}),
            deepLinks = listOf(
                navDeepLink { uriPattern = "todoapp://add_edit/{id}" }
            )
        ) {
            val addEditFragmentComponent = (LocalContext.current.applicationContext as Application)
                .appComponent.addEditFragmentComponentFactory().create()
            Inject(composeViewModelFactory = addEditFragmentComponent.getViewModelFactory()) {
                AddEditScreen(
                    navController
                )
            }
        }
        composable(
            "settings"
        ){
            val settingsFragmentComponent = (LocalContext.current.applicationContext as Application)
                .appComponent.settingsFragmentComponentFactory().create()
            Inject(settingsFragmentComponent.getViewModelFactory()) {
                SettingsScreen(
                    navController
                )
            }
        }
        composable(
            "auth"
        ){
            val authFragmentComponent = (LocalContext.current.applicationContext as Application)
                .appComponent.authFragmentComponentFactory().create()
            Inject(authFragmentComponent.getViewModelFactory()) {
                AuthScreen(
                    navController
                )
            }
        }
    }
}