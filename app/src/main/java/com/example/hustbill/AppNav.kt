package com.example.hustbill

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandIn
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
import com.example.hustbill.ui.screen.add.AddScreen
import com.example.hustbill.ui.screen.index.AppScaffold
import com.example.hustbill.ui.theme.Gap
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNav(nav: NavHostController = rememberAnimatedNavController()) {
    val paddingValues = PaddingValues(horizontal = Gap.Large)
    AnimatedNavHost(
        navController = nav,
        startDestination =AppRoute.MAIN
    ) {
        slideAnimateCompose(
            AppRoute.MAIN,
        ) {
            AppScaffold(contentPadding = paddingValues)
        }

        slideAnimateCompose(
            AppRoute.ADD
        ){
            AddScreen(contentPadding = paddingValues)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.slideAnimateCompose(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) =
    composable(
        route,
        enterTransition = {
            if(this.targetState.destination.route==AppRoute.ADD
                ||this.initialState.destination.route==AppRoute.ADD){
                fadeIn()
            }else{
                slideInHorizontally(initialOffsetX = { it })
            }
        },
        exitTransition = {
            if(this.targetState.destination.route==AppRoute.ADD
                ||this.initialState.destination.route==AppRoute.ADD){
                fadeOut()
            }else{
                slideOutHorizontally(targetOffsetX = { -it  })
            }
        },
        popEnterTransition = {
            if(this.targetState.destination.route==AppRoute.ADD
                ||this.initialState.destination.route==AppRoute.ADD){
                fadeIn()
            }else{
                slideInHorizontally(initialOffsetX = { -it })
            }
        },
        popExitTransition = {
            if(this.targetState.destination.route==AppRoute.ADD
                ||this.initialState.destination.route==AppRoute.ADD){
                fadeOut()
            }else{
                slideOutHorizontally(targetOffsetX = { it  })
            }
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
    const val ADD = "add"
}