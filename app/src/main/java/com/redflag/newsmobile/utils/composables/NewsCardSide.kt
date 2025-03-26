package com.redflag.newsmobile.utils.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.redflag.newsmobile.data.remote.model.Article

@Composable
fun NewsCardSide(article: Article, modifier: Modifier?, onClick: () -> Unit) {
    Surface (modifier = Modifier.padding(bottom = 6.dp)) {
        Column (modifier = Modifier.padding(6.dp)){
            if (article.urlToImage != null) {
                Log.d("Image", article.urlToImage.toString())
                Row(horizontalArrangement = Arrangement.SpaceAround) {
                    AsyncImage(
                        model = article.urlToImage.toString(),
                        contentDescription = "teste",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(120.dp)
                    )
                    Text(text = article.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(6.dp))
                }
            }
            else {
                Text(text = article.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
//            Column (modifier = Modifier.padding(6.dp)){
            if (article.description != null) {
                Text(text = article.description, fontSize = 16.sp)
            }
            Row (modifier= Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = article.author?: "", fontSize = 14.sp)
                Text(text = article.publishedAt, fontSize = 14.sp)
                //Text(text = article.source.name, fontSize = 14.sp)
            }
//            }
        }
    }
}