package com.redflag.newsmobile.data.remote.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.redflag.newsmobile.data.remote.database.dao.CatalogDao
import com.redflag.newsmobile.data.remote.database.entities.ArticleItem
import com.redflag.newsmobile.data.remote.database.entities.Catalog

@Database(entities = [Catalog::class, ArticleItem::class], version = 1, exportSchema = true)
abstract class AppDatabase: RoomDatabase() {
    abstract fun catalogDao(): CatalogDao
}