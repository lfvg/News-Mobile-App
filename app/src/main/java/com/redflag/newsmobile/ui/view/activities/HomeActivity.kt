package com.redflag.newsmobile.ui.view.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import androidx.room.Room
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.redflag.newsmobile.R
import com.redflag.newsmobile.data.HomeScreen
import com.redflag.newsmobile.data.remote.database.AppDatabase
import com.redflag.newsmobile.data.remote.database.dao.CatalogDao
import com.redflag.newsmobile.data.remote.database.entities.Catalog
import com.redflag.newsmobile.data.remote.model.Article
import com.redflag.newsmobile.notification.SampleNotificationService
import com.redflag.newsmobile.ui.theme.NewsMobileTheme
import com.redflag.newsmobile.ui.viewModel.HomeViewModel
import com.redflag.newsmobile.ui.viewModel.SearchViewModel
import com.redflag.newsmobile.utils.composables.NewsCard
import com.redflag.newsmobile.utils.composables.NewsCardSide
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.coroutines.coroutineContext


class HomeActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "news-db.db"
        ).build()
        val catalogDao = db.catalogDao()


        enableEdgeToEdge()
        setContent {
            val postNotificationPermission = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
            val sampleNotificationService = SampleNotificationService(this)

            LaunchedEffect(key1 = true) {
                if(!postNotificationPermission.status.isGranted) {
                    postNotificationPermission.launchPermissionRequest()
                }
            }

            val navController = rememberNavController()
            NewsMobileTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeView(
                        navController, catalogDao, sampleNotificationService
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeView(navHostController: NavHostController, catalogDao: CatalogDao, sampleNotificationService: SampleNotificationService, modifier: Modifier = Modifier) {
    var bottomMenuSelectedItem by remember { mutableStateOf(HomeScreen.Start) }
    val homeViewModel: HomeViewModel = HomeViewModel()
    val searchViewModel: SearchViewModel = SearchViewModel()

    val scope = rememberCoroutineScope()
    val catalogList by catalogDao.getAll().collectAsState(initial = emptyList())

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
                                val context = LocalContext.current
                                val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(article.url.toString()))}
                                if(article.title.length < 80) {
                                    NewsCard(
                                        article = article, modifier = Modifier,
                                        onClick = {
                                            context.startActivity(intent)
                                        }
                                    )
                                }
                                else {
                                    NewsCardSide(article = article, modifier = Modifier, onClick = { context.startActivity(intent) })
                                }
                        }
                    }
                }
                composable(route = HomeScreen.Bookmark.name) {
                    var showDialog by remember { mutableStateOf(false) }
                    var inputText by remember { mutableStateOf("") }
                    val navController = rememberNavController()

                    Scaffold(
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = { showDialog = true },
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar")
                            }
                        }
                    ) { paddingValues ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            catalogList.forEach { item ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding( 10.dp, 0.dp, 0.dp, 10.dp),
                                    shape = MaterialTheme.shapes.medium,
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = item.title,
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.weight(1f)
                                        )
                                        IconButton(onClick = {
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Settings,
                                                contentDescription = "Configurações"
                                            )
                                        }

                                        IconButton(onClick = {
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Remover"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Criar catalogo") },
                            text = {
                                OutlinedTextField(
                                    value = inputText,
                                    onValueChange = { inputText = it },
                                    label = { Text("Digite algo") }
                                )
                            },
                            confirmButton = {
                                Button(onClick = {
                                    val newCatalog = Catalog(
                                        id = UUID.randomUUID().toString(),
                                        title = inputText
                                    )
                                    scope.launch {
                                        catalogDao.insert(newCatalog)
                                    }
                                    sampleNotificationService.showBasicNotification()
                                    inputText = "" // zerando o inputText depois de clicar o botão !
                                    showDialog = false
                                }) {
                                    Text("Criar")
                                }
                            },
                            dismissButton = {
                                Button(onClick = {
                                    showDialog = false
                                    navController.navigate(route = HomeScreen.Bookmark.name) // Voltar para HomeScreen
                                }) {
                                    Text("Cancelar")
                                }
                            }
                        )
                    }
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

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    NewsMobileTheme {
//        HomeView(rememberNavController())
//    }
//}