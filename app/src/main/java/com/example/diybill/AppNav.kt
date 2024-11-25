package com.example.diybill

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.navArgument
import com.example.diybill.ui.screen.add.AddScreen
import com.example.diybill.ui.screen.index.AppScaffold
import com.example.diybill.ui.screen.update.UpdateScreen
import com.example.diybill.ui.theme.Gap
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNav(nav: NavHostController = rememberAnimatedNavController()) {
    val paddingValues = PaddingValues(horizontal = Gap.Large)
    AnimatedNavHost(
        navController = nav,
        startDestination = AppRoute.MAIN
    ) {
        slideAnimateCompose(
            AppRoute.MAIN,
        ) {
            AppScaffold(contentPadding = paddingValues)
        }

        slideAnimateCompose(
            AppRoute.ADD
        ) {
            AddScreen(contentPadding = paddingValues)
        }

        slideAnimateCompose(
            route = AppRoute.UPDATE + "?id={id}",
            arguments = listOf(navArgument("id") { defaultValue = -1 })
        ) {
            UpdateScreen(
                it.arguments?.getInt("id") ?: -1,
                contentPadding = paddingValues
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.slideAnimateCompose(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit,
) =
    composable(
        route,
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
    const val ADD = "add"
    const val UPDATE = "update"
}