package com.example.swipeassignment.room.daointerface

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.swipeassignment.datamodel.ProductItems

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: ProductItems)

    @Query("SELECT * FROM ProductItems")
    suspend fun getAllData(): List<ProductItems>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPending(data: ProductItems)

    @Query("SELECT * FROM ProductItems WHERE isPending = 1")
    suspend fun getAllPending(): List<ProductItems>

    @Query("UPDATE ProductItems SET isPending = 2 WHERE id = :id")
    suspend fun removePending(id: Int)

    @Query("DELETE FROM ProductItems WHERE isPending = 0")
    suspend fun clearAllData()

    @Query("SELECT * FROM ProductItems WHERE isPending = 2")
    suspend fun getAddedData(): List<ProductItems>
}