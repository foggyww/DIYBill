package com.example.hustbill

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.example.hustbill.ui.screen.index.AppScaffold
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNav(nav: NavHostController = rememberAnimatedNavController()) {
    AnimatedNavHost(
        navController = nav,
        startDestination =AppRoute.MAIN
    ) {
        animateCompose(
            AppRoute.MAIN,
        ) {
            AppScaffold()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.animateCompose(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) =
    composable(
        route,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { it })
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -it  })
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -it  })
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it })
        },
        arguments = arguments,
        deepLinks = deepLinks,
        content = content
    )

fun NavHostController.replace(route: String) {
    this.popBackStack()
    this.navigate(
        route, NavOptions.Builder()
            .setEnterAnim(R.anim.enter)
            .setExitAnim(R.anim.exit)
            .setPopUpTo(AppRoute.MAIN, true).build()
    )
}

fun NavHostController.push(route: String) {
    this.navigate(
        route, NavOptions.Builder()
            .setEnterAnim(R.anim.enter)
            .setExitAnim(R.anim.exit)
            .build()
    )
}

fun NavHostController.pop() {
    this.popBackStack()
}

object AppRoute {
    const val MAIN = "main"
}