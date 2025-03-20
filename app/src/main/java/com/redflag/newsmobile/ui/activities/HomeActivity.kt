package com.redflag.newsmobile.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.redflag.newsmobile.R
import com.redflag.newsmobile.data.HomeScreen
import com.redflag.newsmobile.ui.theme.NewsMobileTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NewsMobileTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeView(
                        navController
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun HomeView(navHostController: NavHostController, modifier: Modifier = Modifier) {
    var bottomMenuSelectedItem by remember { mutableStateOf(HomeScreen.Start) }
    Scaffold(bottomBar = {
        NavigationBar {
            NavigationBarItem(selected = bottomMenuSelectedItem == HomeScreen.Start,
                onClick = {
                    if(bottomMenuSelectedItem != HomeScreen.Start) {
                        bottomMenuSelectedItem = HomeScreen.Start
                        navHostController.navigate(HomeScreen.Start.name)
//                        LaunchedEffect("HOME_START") {
//
//                        }
//                        coroutineScope {
//                            launch {
//
//                            }
//                        }
                    }
                },
                icon = {
                    if (bottomMenuSelectedItem != HomeScreen.Start)
                        Icon(
                            painter = painterResource(id = R.drawable.home),
                            contentDescription = ""
                        )
                    else
                        Icon(
                            painter = painterResource(id = R.drawable.home_filled),
                            contentDescription = ""
                        )
                }
            )
            NavigationBarItem(selected = bottomMenuSelectedItem == HomeScreen.Bookmark,
                onClick = {
                    if(bottomMenuSelectedItem != HomeScreen.Bookmark) {
                        bottomMenuSelectedItem = HomeScreen.Bookmark
                        navHostController.navigate(HomeScreen.Bookmark.name)
                    }
                },
                icon = {
                    if (bottomMenuSelectedItem != HomeScreen.Bookmark)
                        Icon(
                            painter = painterResource(id = R.drawable.bookmark),
                            contentDescription = ""
                        )
                    else
                        Icon(
                            painter = painterResource(id = R.drawable.bookmark_selected),
                            contentDescription = ""
                        )
                }
            )
            NavigationBarItem(selected = bottomMenuSelectedItem == HomeScreen.Search,
                onClick = {
                    if (bottomMenuSelectedItem != HomeScreen.Search) {
                        bottomMenuSelectedItem = HomeScreen.Search
                        navHostController.navigate(HomeScreen.Search.name)
                    }
                },
                icon = {
                    if (bottomMenuSelectedItem != HomeScreen.Search)
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = ""
                        )
                    else
                        Icon(
                            painter = painterResource(id = R.drawable.search_selected),
                            contentDescription = ""
                        )
                }
            )
            NavigationBarItem(selected = bottomMenuSelectedItem == HomeScreen.Settings,
                onClick = {
                    if(bottomMenuSelectedItem != HomeScreen.Settings) {
                       bottomMenuSelectedItem = HomeScreen.Settings
                        navHostController.navigate(HomeScreen.Settings.name)
                    }
                },
                icon = {
                    if (bottomMenuSelectedItem != HomeScreen.Settings)
                        Icon(
                            painter = painterResource(id = R.drawable.settings),
                            contentDescription = ""
                        )
                    else
                        Icon(
                            painter = painterResource(id = R.drawable.settings_selected),
                            contentDescription = ""
                        )
                }
            )
        }

    }) { innerPadding ->
        Surface(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {

            NavHost(navController = navHostController, startDestination = HomeScreen.Start.name){
                composable(route = HomeScreen.Start.name) {
//                    CoroutineScope(Dispatchers.IO).launch {
//
//                    }
                    LaunchedEffect("HOME_START") {
                        coroutineScope {
                            launch {

                            }
                        }
                    }
                    Text(
                        text = "Home Screen!",
                    )
                }
                composable(route = HomeScreen.Bookmark.name) {
                    Text(
                        text = "Bookmark Screen!",
                    )
                }
                composable(route = HomeScreen.Search.name) {
                    Text(
                        text = "Search Screen!",
                    )
                }
                composable(route = HomeScreen.Settings.name) {
                    Text(
                        text = "Settings Screen!",
                    )
                }
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NewsMobileTheme {
        HomeView(rememberNavController())
    }
}