package com.redflag.newsmobile.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.redflag.newsmobile.R
import com.redflag.newsmobile.data.HomeScreen
import com.redflag.newsmobile.data.remote.model.Article
import com.redflag.newsmobile.ui.theme.NewsMobileTheme
import com.redflag.newsmobile.ui.viewModel.HomeViewModel
import com.redflag.newsmobile.ui.viewModel.SearchViewModel
import com.redflag.newsmobile.utils.composables.NewsCard
import com.redflag.newsmobile.utils.composables.NewsCardSide


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

@OptIn(ExperimentalAnimationGraphicsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeView(navHostController: NavHostController, modifier: Modifier = Modifier) {
    var bottomMenuSelectedItem by remember { mutableStateOf(HomeScreen.Start) }
    val homeViewModel: HomeViewModel = HomeViewModel()
    val searchViewModel: SearchViewModel = SearchViewModel()

    Scaffold(bottomBar = {
        NavigationBar {
            NavigationBarItem(selected = bottomMenuSelectedItem == HomeScreen.Start,
                onClick = {
                    if(bottomMenuSelectedItem != HomeScreen.Start) {
                        bottomMenuSelectedItem = HomeScreen.Start
                        navHostController.navigate(HomeScreen.Start.name)
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
                    val data by homeViewModel.data.collectAsState()
                    Column ( modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(4.dp),
                        verticalArrangement = Arrangement.SpaceEvenly){
                            data?.forEach { article: Article ->
                                if(article.title.length < 80) {
                                    NewsCard(
                                        article = article, modifier = Modifier,
                                        onClick = { }
                                    )
                                }
                                else {
                                    NewsCardSide(article = article, modifier = Modifier, onClick = { })
                                }
                        }
                    }
                }
                composable(route = HomeScreen.Bookmark.name) {
                    Text(
                        text = "Bookmark Screen!",
                    )
                }
                composable(route = HomeScreen.Search.name) {
                    val placeholder = emptyList<String>()
                    val searchData by searchViewModel.data.collectAsState()
                    var expanded by rememberSaveable { mutableStateOf(false) }
                    var queryText by rememberSaveable { mutableStateOf("") }

                    Box(
                        modifier
                            .fillMaxSize()
                            .semantics { isTraversalGroup = true }
                    ) {
                        SearchBar(
                            query = queryText,
                            onQueryChange = { queryText = it },
                            onSearch = {
                                searchViewModel.search(queryText)
                                expanded = false
                                       },
                            active = expanded,
                            onActiveChange = { expanded = it },
                            placeholder = { Text(text = "Search for news") },
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .semantics { traversalIndex = 0f }

                        ) {

                        }
                        Column(Modifier.verticalScroll(rememberScrollState())) {
                            searchData.toString()
                            searchData?.forEach { result ->
                                if (result.title != null)
                                    Text(text = result.title)
                            }
                        }
                    }
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