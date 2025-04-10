package com.redflag.newsmobile.ui.view.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.redflag.newsmobile.R
import com.redflag.newsmobile.data.HomeScreen
import com.redflag.newsmobile.data.remote.database.AppDatabase
import com.redflag.newsmobile.data.remote.database.dao.CatalogDao
import com.redflag.newsmobile.data.remote.database.entities.Catalog
import com.redflag.newsmobile.data.remote.model.Article
import com.redflag.newsmobile.data.remote.service.DataSyncService
import com.redflag.newsmobile.ui.theme.NewsMobileTheme
import com.redflag.newsmobile.ui.viewModel.HomeViewModel
import com.redflag.newsmobile.ui.viewModel.SearchViewModel
import com.redflag.newsmobile.utils.composables.NewsCard
import com.redflag.newsmobile.utils.composables.NewsCardSide
import kotlinx.coroutines.launch
import java.util.UUID
// Importações para a nova API de pull-to-refresh
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import com.google.accompanist.permissions.isGranted

class HomeActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o banco de dados
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "news-db.db"
        ).build()
        val catalogDao: CatalogDao = db.catalogDao()

        enableEdgeToEdge()
        setContent {
            // Solicita permissão para notificações
            val postNotificationPermission =
                rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
            LaunchedEffect(key1 = true) {
                if (!postNotificationPermission.status.isGranted) {
                    postNotificationPermission.launchPermissionRequest()
                }
            }

            // Inicia o serviço de sincronização de notícias
            startService(Intent(this, DataSyncService::class.java))

            val navController = rememberNavController()
            NewsMobileTheme {
                HomeView(navHostController = navController, catalogDao = catalogDao)
            }
        }
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeView(
    navHostController: NavHostController,
    catalogDao: CatalogDao,
    modifier: Modifier = Modifier
) {
    var bottomMenuSelectedItem by remember { mutableStateOf(HomeScreen.Start) }
    val homeViewModel: HomeViewModel = HomeViewModel()
    val searchViewModel: SearchViewModel = SearchViewModel()
    val scope = rememberCoroutineScope()
    val catalogList by catalogDao.getAll().collectAsState(initial = emptyList())
    var isRefreshing by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(bottomBar = {
        NavigationBar {
            NavigationBarItem(
                selected = bottomMenuSelectedItem == HomeScreen.Start,
                onClick = {
                    if (bottomMenuSelectedItem != HomeScreen.Start) {
                        bottomMenuSelectedItem = HomeScreen.Start
                        navHostController.navigate(HomeScreen.Start.name)
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (bottomMenuSelectedItem != HomeScreen.Start)
                                R.drawable.home else R.drawable.home_filled
                        ),
                        contentDescription = ""
                    )
                }
            )
            NavigationBarItem(
                selected = bottomMenuSelectedItem == HomeScreen.Bookmark,
                onClick = {
                    if (bottomMenuSelectedItem != HomeScreen.Bookmark) {
                        bottomMenuSelectedItem = HomeScreen.Bookmark
                        navHostController.navigate(HomeScreen.Bookmark.name)
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (bottomMenuSelectedItem != HomeScreen.Bookmark)
                                R.drawable.bookmark else R.drawable.bookmark_selected
                        ),
                        contentDescription = ""
                    )
                }
            )
            NavigationBarItem(
                selected = bottomMenuSelectedItem == HomeScreen.Search,
                onClick = {
                    if (bottomMenuSelectedItem != HomeScreen.Search) {
                        bottomMenuSelectedItem = HomeScreen.Search
                        navHostController.navigate(HomeScreen.Search.name)
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (bottomMenuSelectedItem != HomeScreen.Search)
                                R.drawable.search else R.drawable.search_selected
                        ),
                        contentDescription = ""
                    )
                }
            )
            NavigationBarItem(
                selected = bottomMenuSelectedItem == HomeScreen.Settings,
                onClick = {
                    if (bottomMenuSelectedItem != HomeScreen.Settings) {
                        bottomMenuSelectedItem = HomeScreen.Settings
                        navHostController.navigate(HomeScreen.Settings.name)
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (bottomMenuSelectedItem != HomeScreen.Settings)
                                R.drawable.settings else R.drawable.settings_selected
                        ),
                        contentDescription = ""
                    )
                }
            )
        }
    }) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            NavHost(
                navController = navHostController,
                startDestination = HomeScreen.Start.name
            ) {
                composable(route = HomeScreen.Start.name) {
                    // Utilizando a nova API de pull-to-refresh
                    val pullRefreshState = rememberPullRefreshState(
                        refreshing = isRefreshing,
                        onRefresh = {
                            scope.launch {
                                isRefreshing = true
                                homeViewModel.refreshData() // Implemente a lógica de atualização na ViewModel
                                isRefreshing = false
                            }
                        }
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pullRefresh(pullRefreshState)
                    ) {
                        val data by homeViewModel.data.collectAsState()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(4.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            data?.forEach { article: Article ->
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url.toString()))
                                if (article.title.length < 80) {
                                    NewsCard(
                                        article = article,
                                        modifier = Modifier,
                                        onClick = { context.startActivity(intent) }
                                    )
                                } else {
                                    NewsCardSide(
                                        article = article,
                                        modifier = Modifier,
                                        onClick = { context.startActivity(intent) }
                                    )
                                }
                            }
                        }
                        // Indicador de pull-to-refresh
                        PullRefreshIndicator(
                            refreshing = isRefreshing,
                            state = pullRefreshState,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                    }
                }
                composable(route = HomeScreen.Bookmark.name) {
                    var showDialog by remember { mutableStateOf(false) }
                    var inputText by remember { mutableStateOf("") }
                    val innerNavController = rememberNavController()

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
                                        .padding(10.dp, 0.dp, 0.dp, 10.dp),
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
                                            // Lógica para configurações do item, se necessário
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Settings,
                                                contentDescription = "Configurações"
                                            )
                                        }
                                        IconButton(onClick = {
                                            // Lógica para remover o item
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
                            title = { Text("Criar catálogo") },
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
                                    inputText = ""
                                    showDialog = false
                                }) {
                                    Text("Criar")
                                }
                            },
                            dismissButton = {
                                Button(onClick = {
                                    showDialog = false
                                    navHostController.navigate(HomeScreen.Bookmark.name)
                                }) {
                                    Text("Cancelar")
                                }
                            }
                        )
                    }
                }
                composable(route = HomeScreen.Search.name) {
                    var expanded by rememberSaveable { mutableStateOf(false) }
                    var queryText by rememberSaveable { mutableStateOf("") }
                    val searchData by searchViewModel.data.collectAsState()

                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .semantics { traversalIndex = 0f }
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
                        ) { }
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            searchData?.forEach { result ->
                                NewsCardSide(article = result, modifier = Modifier, onClick = { })
                            }
                        }
                    }
                }
                composable(route = HomeScreen.Settings.name) {
                    Text(text = "Settings Screen!")
                }
            }
        }
    }
}
