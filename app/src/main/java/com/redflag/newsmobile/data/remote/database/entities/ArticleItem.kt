package com.redflag.newsmobile.data.remote.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
        entity = Catalog::class,
        parentColumns = ["id"],
        childColumns = ["catalogId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ArticleItem(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val catalogId: String
)