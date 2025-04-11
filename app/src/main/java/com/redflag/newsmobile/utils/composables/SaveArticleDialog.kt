package com.redflag.newsmobile.utils.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.redflag.newsmobile.data.remote.database.entities.Catalog

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