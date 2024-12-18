package com.example.diybill.ui.screen.index

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.example.diybill.AppRoute
import com.example.diybill.R
import com.example.diybill.ui.provider.LocalNav
import com.example.diybill.ui.theme.Gap
import com.example.diybill.ui.theme.ImageSize
import com.example.diybill.ui.theme.RoundedShapes
import com.example.diybill.ui.theme.colors
import com.example.diybill.ui.widgets.EasyImage
import com.example.diybill.utils.click
import kotlinx.coroutines.launch

@Composable
fun BottomNavBar(
    page: PagerState,
){
    val bottomNavList = remember {
        listOf(
            BottomNavRoute.Home,
            BottomNavRoute.Type,
            BottomNavRoute.Advices,
            BottomNavRoute.Setting,
        )
    }
    val scope = rememberCoroutineScope()
    val height = remember {
        mutableIntStateOf(0)
    }
    val width = remember {
        mutableIntStateOf(0)
    }
    val nav = LocalNav.current
    Box(modifier = Modifier.height(64.dp)){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .align(Alignment.BottomCenter)
                .border(0.3.dp, colors.geryBackground)
                .shadow(Gap.Mid)
                .background(colors.background)
                .onGloballyPositioned {
                    height.intValue = it.size.height
                    width.intValue = it.size.width
                },
        ) {
            bottomNavList.forEach { screen ->
                val selected = page.currentPage == screen.routeNumber
                NavigationBarItem(
                    enabled = screen.routeNumber != -1,
                    modifier = Modifier.background(Color.Transparent),
                    selected = false,
                    icon = {
                        EasyImage(
                            modifier = Modifier
                                .size(ImageSize.Mid+4.dp),
                            src = screen.icon,
                            contentDescription = screen.string,
                            tint = if(selected) colors.secondary else colors.unfocused,
                        )
                    },
                    onClick = {
                        scope.launch {
                            page.scrollToPage(screen.routeNumber)
                        }
                    }
                )
            }
        }
        Box(modifier = Modifier
            .offset(0.dp,(-32).dp)
            .align(Alignment.TopCenter)){
            EasyImage(
                modifier = Modifier.size(60.dp)
                    .align(Alignment.TopCenter)
                    .clip(RoundedShapes.large)
                    .shadow(Gap.Small,RoundedShapes.large)
                    .click {
                        scope.launch {
                            nav.navigate(AppRoute.ADD)
                        }
                    }
                , src = if(page.currentPage!=-1) R.drawable.add_yellow else R.drawable.add_pink,
                contentDescription = "添加",
                scale = ContentScale.Fit)
        }
    }
}