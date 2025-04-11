package com.redflag.newsmobile.data.remote.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CatalogWithArticles(
    @Embedded val catalog: Catalog,
    @Relation(
        parentColumn = "id",
        entityColumn = "catalogId"
    )
    val articles: List<ArticleItem>
)