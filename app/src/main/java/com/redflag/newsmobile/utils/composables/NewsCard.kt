package com.redflag.newsmobile.utils.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.redflag.newsmobile.data.remote.database.dao.CatalogDao
import com.redflag.newsmobile.data.remote.database.entities.ArticleItem
import com.redflag.newsmobile.data.remote.database.entities.Catalog
import com.redflag.newsmobile.data.remote.model.Article
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Composable
fun NewsCard(article: Article, modifier: Modifier?, onClick: () -> Unit, catalogDao: CatalogDao) {
    val catalogList by catalogDao.getAll().collectAsState(initial = emptyList())
    var date  = ""
    if (article.publishedAt != null) {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val formattedDate = LocalDateTime.parse(article.publishedAt, dateFormatter)
        date = DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mma")
            .format(formattedDate) //  04 August 2017, 6:39pm
    }
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Surface (modifier = Modifier.padding(bottom = 6.dp).clickable(onClick = onClick)) {
        Column (modifier = Modifier.padding(6.dp)){
            if (article.urlToImage != null) {
                Log.d("Image", article.urlToImage.toString())
                Box() {
                    AsyncImage(
                        model = article.urlToImage.toString(),
                        contentDescription = "Article image"
                    )
                    Text(text = article.title, fontSize = 20.sp, fontWeight = FontWeight.Bold,
                        style = TextStyle(
                        shadow = Shadow(
                            color = MaterialTheme.colorScheme.inverseOnSurface, blurRadius = 1f
                        )), modifier = Modifier.align(Alignment.BottomStart).background(
                            Brush.verticalGradient(colors = listOf(
                            Color.Transparent, MaterialTheme.colorScheme.surface))).padding(6.dp))
                }
            }
            else {
                Text(text = article.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

                if (article.description != null) {
                    Text(text = article.description, fontSize = 16.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
                Row (
                    modifier= Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Column {
                        Text(
                            text = article.author ?: "",
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = date,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        IconButton(onClick = { showDialog = true }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = "Salvar artigo",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

        }
    }
    if (showDialog) {
        SaveArticleDialog(
            catalogList = catalogList,
            onDismiss = { showDialog = false },
            onSelectCatalog = { catalog ->
                scope.launch {
                    val articleItem = ArticleItem(
                        id = UUID.randomUUID().toString(),
                        catalogId = catalog.id,
                        title = article.title ?: "Sem título",
                        description = article.description,
                        url = article.url.toString(),
                        urlToImage = article.urlToImage.toString()
                    )
                    catalogDao.insertArticle(articleItem)
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun SaveArticleDialog(catalogList: List<Catalog>, onDismiss: () -> Unit, onSelectCatalog: (Catalog) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Salvar em catalogo") },
        text = {
            if (catalogList.isEmpty()) {
                Text("Nenhum catalogo disponível.")
            } else {
                Column {
                    catalogList.forEach { catalog ->
                        OutlinedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    onSelectCatalog(catalog)
                                }
                        ) {
                            Text(
                                text = catalog.title,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}