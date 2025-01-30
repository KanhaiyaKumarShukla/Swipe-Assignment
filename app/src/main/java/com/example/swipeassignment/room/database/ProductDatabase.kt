package com.example.swipeassignment.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.swipeassignment.datamodel.ProductItems
import com.example.swipeassignment.room.daointerface.ProductDao

// Room Database
@Database(entities = [ProductItems::class], version = 1, exportSchema = false)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}