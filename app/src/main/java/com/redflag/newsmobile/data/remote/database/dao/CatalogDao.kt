package com.redflag.newsmobile.data.remote.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.redflag.newsmobile.data.remote.database.entities.Catalog
import kotlinx.coroutines.flow.Flow

@Dao
interface CatalogDao {
    @Query("SELECT * FROM Catalog")
    fun getAll(): Flow<List<Catalog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg catalog: Catalog)
}