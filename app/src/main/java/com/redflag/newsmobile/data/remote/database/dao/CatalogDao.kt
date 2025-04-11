package com.redflag.newsmobile.data.remote.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.redflag.newsmobile.data.remote.database.entities.ArticleItem
import com.redflag.newsmobile.data.remote.database.entities.Catalog
import com.redflag.newsmobile.data.remote.database.entities.CatalogWithArticles
import kotlinx.coroutines.flow.Flow

@Dao
interface CatalogDao {
    @Query("SELECT * FROM Catalog")
    fun getAll(): Flow<List<Catalog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg catalog: Catalog)

    @Delete
    suspend fun delete(catalog: Catalog)

    @Update
    suspend fun update(catalog: Catalog)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: ArticleItem)

    @Transaction
    @Query("SELECT * FROM Catalog WHERE id = :catalogId")
    fun getCatalogWithArticles(catalogId: String): Flow<CatalogWithArticles>
}